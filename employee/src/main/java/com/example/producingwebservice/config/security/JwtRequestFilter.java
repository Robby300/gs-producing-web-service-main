package com.example.producingwebservice.config.security;

import com.example.producingwebservice.api.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer ";
	public static final String UNABLE_TO_GET_JWT_TOKEN = "Unable to get JWT Token ";
	public static final String JWT_TOKEN_HAS_EXPIRED = "JWT Token has expired ";
	public static final String JWT_TOKEN_DOES_NOT_BEGIN_WITH_BEARER_STRING =
			"JWT Token does not begin with Bearer String";
	public static final String BLACKLIST_PREFIX = "blacklist:jwt:";
	private final UserService jwtUserDetailsService;
	private final JwtTokenUtil jwtTokenUtil;
	private final StringRedisTemplate stringRedisTemplate;

	public JwtRequestFilter(
			UserService jwtUserDetailsService,
			JwtTokenUtil jwtTokenUtil,
			StringRedisTemplate stringRedisTemplate) {
		this.jwtUserDetailsService = jwtUserDetailsService;
		this.jwtTokenUtil = jwtTokenUtil;
		this.stringRedisTemplate = stringRedisTemplate;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return path.startsWith("/actuator/") || path.startsWith("/swagger-ui/") || path.startsWith("/v3/api-docs");
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader(AUTHORIZATION);

		String username = null;
		String jwtToken = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith(BEARER)) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				if (isTokenBlacklisted(jwtToken)) {
					log.warn("Blacklisted JWT token rejected");
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has been revoked");
					return;
				}
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				log.error(UNABLE_TO_GET_JWT_TOKEN, e);
			} catch (ExpiredJwtException e) {
				log.error(JWT_TOKEN_HAS_EXPIRED, e);
			}
		} else {
			log.warn(JWT_TOKEN_DOES_NOT_BEGIN_WITH_BEARER_STRING);
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

			if (Boolean.TRUE.equals(jwtTokenUtil.validateToken(jwtToken, userDetails))) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}

	private boolean isTokenBlacklisted(String jwtToken) {
		try {
			String tokenHash = sha256(jwtToken);
			return Boolean.TRUE.equals(stringRedisTemplate.hasKey(BLACKLIST_PREFIX + tokenHash));
		} catch (Exception e) {
			log.warn("Redis unavailable for blacklist check, allowing request", e);
			return false;
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
