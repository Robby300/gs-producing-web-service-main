package com.example.producingwebservice.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Order(1)
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

	private static final long LOGIN_LIMIT = 5;
	private static final long REGISTRATION_LIMIT = 2;
	private static final long WINDOW_SECONDS = 60;

	private final StringRedisTemplate stringRedisTemplate;

	public RateLimitFilter(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain chain) throws ServletException, IOException {

		String path = request.getRequestURI();
		String method = request.getMethod();

		if ("POST".equalsIgnoreCase(method) && "/login".equals(path)) {
			if (isRateLimited("ratelimit:login:", request)) {
				response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "Too many login attempts");
				return;
			}
		} else if ("POST".equalsIgnoreCase(method) && "/registration".equals(path)) {
			if (isRateLimited("ratelimit:registration:", request)) {
				response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "Too many registration attempts");
				return;
			}
		}

		chain.doFilter(request, response);
	}

	private boolean isRateLimited(String prefix, HttpServletRequest request) {
		String key = prefix + getClientIp(request);
		try {
			Long count = stringRedisTemplate.opsForValue().increment(key);
			if (count != null && count == 1) {
				stringRedisTemplate.expire(key, WINDOW_SECONDS, TimeUnit.SECONDS);
			}
			long limit = prefix.contains("login") ? LOGIN_LIMIT : REGISTRATION_LIMIT;
			return count != null && count > limit;
		} catch (Exception e) {
			log.warn("Redis unavailable for rate limiting, allowing request", e);
			return false;
		}
	}

	private String getClientIp(HttpServletRequest request) {
		String xf = request.getHeader("X-Forwarded-For");
		if (xf != null && !xf.isBlank()) {
			return xf.split(",")[0].trim();
		}
		return request.getRemoteAddr();
	}
}
