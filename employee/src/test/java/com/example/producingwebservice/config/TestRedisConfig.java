package com.example.producingwebservice.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestRedisConfig {

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return mock(RedisConnectionFactory.class);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory cf) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(cf);
		return template;
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory cf) {
		return new StringRedisTemplate(cf);
	}
}
