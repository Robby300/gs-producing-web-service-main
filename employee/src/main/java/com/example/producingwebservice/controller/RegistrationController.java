package com.example.producingwebservice.controller;


import com.example.producingwebservice.api.UserService;
import com.example.producingwebservice.entity.User;
import com.example.producingwebservice.model.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Registration controller")
public class RegistrationController {
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	public RegistrationController(UserService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/registration")
	@Operation(summary = "Registration new user")
	public User addUser(@RequestBody UserDto userDto) {
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		log.info("Registration new user = {}", userDto);
		return userService.save(userDto);
	}
}
