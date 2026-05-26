package com.example.producingwebservice.controller;

import com.example.producingwebservice.api.UserService;
import com.example.producingwebservice.config.security.JwtTokenUtil;
import com.example.producingwebservice.model.JwtRequest;
import com.example.producingwebservice.model.JwtResponse;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin
@Tag(name = "Authentication controller")
public class JwtAuthenticationController {
	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;
	private final UserService jwtInMemoryUserDetailsService;
	private final StringRedisTemplate stringRedisTemplate;
	private final MeterRegistry meterRegistry;

	public JwtAuthenticationController(
			AuthenticationManager authenticationManager,
			JwtTokenUtil jwtTokenUtil,
			UserService jwtInMemoryUserDetailsService,
			StringRedisTemplate stringRedisTemplate,
			MeterRegistry meterRegistry) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.jwtInMemoryUserDetailsService = jwtInMemoryUserDetailsService;
		this.stringRedisTemplate = stringRedisTemplate;
		this.meterRegistry = meterRegistry;
	}

	@PostMapping("/login")
	@Operation(summary = "Login user")
	public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails =
				jwtInMemoryUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);
		meterRegistry.counter("employee.login.total").increment();
		log.info("Processing POST request /login");
		return ResponseEntity.ok(new JwtResponse(token));
	}

	@PostMapping("/logout")
	@Operation(summary = "Logout user (revoke JWT token)")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.badRequest().build();
		}
		String token = authHeader.substring(7);
		try {
			Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);
			long ttl = expiration.getTime() - System.currentTimeMillis();
			if (ttl > 0) {
				String tokenHash = sha256(token);
				stringRedisTemplate.opsForValue().set(
						"blacklist:jwt:" + tokenHash, "revoked",
						Duration.ofMillis(ttl));
				log.info("Token revoked, expires in {} ms", ttl);
			}
		} catch (Exception e) {
			log.warn("Failed to revoke token", e);
			return ResponseEntity.badRequest().build();
		}
		meterRegistry.counter("employee.logout.total").increment();
		return ResponseEntity.ok().build();
	}

	private void authenticate(String username, String password) {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new IllegalArgumentException("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new IllegalArgumentException("INVALID_CREDENTIALS", e);
		}
	}

	private String sha256(String input) {
		try {
			var digest = java.security.MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
			var hexString = new StringBuilder();
			for (byte b : hash) {
				hexString.append(String.format("%02x", b));
			}
			return hexString.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 not available", e);
		}
	}
}
