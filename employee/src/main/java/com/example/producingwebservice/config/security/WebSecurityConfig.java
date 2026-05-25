package com.example.producingwebservice.config.security;

import com.example.producingwebservice.api.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final UserService jwtUserDetailsService;
	private final JwtRequestFilter jwtRequestFilter;
	private final PasswordEncoder passwordEncoder;

	private static final String[] AUTH_WHITELIST = {
			"/swagger-ui/**",
			"/v3/api-docs/**",
			"/login",
			"/logout",
			"/registration",
			"/actuator/**"
	};

	public WebSecurityConfig(
			JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
			UserService jwtUserDetailsService,
			JwtRequestFilter jwtRequestFilter,
			PasswordEncoder passwordEncoder) {
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.jwtUserDetailsService = jwtUserDetailsService;
		this.jwtRequestFilter = jwtRequestFilter;
		this.passwordEncoder = passwordEncoder;
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		var provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		provider.setUserDetailsService(jwtUserDetailsService);
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(AUTH_WHITELIST).permitAll()
				.anyRequest().authenticated())
			.exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authenticationProvider(authenticationProvider());
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
