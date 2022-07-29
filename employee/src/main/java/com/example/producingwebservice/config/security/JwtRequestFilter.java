package com.example.producingwebservice.config.security;


import com.example.producingwebservice.api.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer ";
	public static final String UNABLE_TO_GET_JWT_TOKEN = "Unable to get JWT Token ";
	public static final String JWT_TOKEN_HAS_EXPIRED = "JWT Token has expired ";
	public static final String JWT_TOKEN_DOES_NOT_BEGIN_WITH_BEARER_STRING =
			"JWT Token does not begin with Bearer String";
	private final UserService jwtUserDetailsService;
	private final JwtTokenUtil jwtTokenUtil;

	public JwtRequestFilter(UserService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {
		this.jwtUserDetailsService = jwtUserDetailsService;
		this.jwtTokenUtil = jwtTokenUtil;
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
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				log.error(UNABLE_TO_GET_JWT_TOKEN + e);
			} catch (ExpiredJwtException e) {
				log.error(JWT_TOKEN_HAS_EXPIRED + e);
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
}
