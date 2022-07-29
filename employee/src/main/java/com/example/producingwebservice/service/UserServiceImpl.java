package com.example.producingwebservice.service;


import com.example.producingwebservice.api.UserService;
import com.example.producingwebservice.entity.User;
import com.example.producingwebservice.model.UserDto;
import com.example.producingwebservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	public static final String NOT_FOUND_USER_WITH_USERNAME = "Not found user with username ";
	private final UserRepository userRepository;
	private final ModelMapper modelMapper = new ModelMapper();

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository
				.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(NOT_FOUND_USER_WITH_USERNAME + username));
	}

	@Override
	public User save(UserDto userDto) {
		return userRepository.save(modelMapper.map(userDto, User.class));
	}
}
