package com.example.producingwebservice.api;

import com.example.producingwebservice.entity.User;
import com.example.producingwebservice.model.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User save(UserDto userDto);
}
