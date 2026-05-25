package com.example.producingwebservice.service;

import com.example.producingwebservice.api.UserService;
import com.example.producingwebservice.entity.User;
import com.example.producingwebservice.model.UserDto;
import com.example.producingwebservice.repository.UserRepository;
import com.example.producingwebservice.type.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	public static final String USER_CACHE_PREFIX = "user:";
	public static final long USER_CACHE_TTL_HOURS = 1;
	private static final String NOT_FOUND_USER_WITH_USERNAME = "Not found user with username ";
	private final UserRepository userRepository;
	private final StringRedisTemplate stringRedisTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String cached = stringRedisTemplate.opsForValue().get(USER_CACHE_PREFIX + username);
		if (cached != null) {
			try {
				return objectMapper.readValue(cached, User.class);
			} catch (JsonProcessingException e) {
				log.warn("Failed to deserialize cached user {}, fallback to DB", username, e);
			}
		}

		User user = userRepository
				.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(NOT_FOUND_USER_WITH_USERNAME + username));

		try {
			String serialized = objectMapper.writeValueAsString(user);
			stringRedisTemplate.opsForValue().set(
					USER_CACHE_PREFIX + username, serialized,
					USER_CACHE_TTL_HOURS, TimeUnit.HOURS);
		} catch (JsonProcessingException e) {
			log.warn("Failed to serialize user {} for cache", username, e);
		}
		return user;
	}

	@Override
	public User save(UserDto userDto) {
		User user = User.builder()
				.username(userDto.getUsername())
				.password(userDto.getPassword())
				.roles(userDto.getRoles())
				.build();
		User saved = userRepository.save(user);
		stringRedisTemplate.delete(USER_CACHE_PREFIX + saved.getUsername());
		return saved;
	}
}
