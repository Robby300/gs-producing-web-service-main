package com.example.producingwebservice.controller;

import com.example.producingwebservice.api.UserService;
import com.example.producingwebservice.domain.User;
import com.example.producingwebservice.model.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RegistrationController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registration")
    public User addUser(@RequestBody UserDto userDto) {
        User user = new User(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        log.info("Registration new user = {}", user.getUsername()); //todo инглиш плиз) // done
        return user;
    }
}
