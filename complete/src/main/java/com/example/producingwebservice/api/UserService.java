package com.example.producingwebservice.api;

import com.example.producingwebservice.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User save(User user);
}
