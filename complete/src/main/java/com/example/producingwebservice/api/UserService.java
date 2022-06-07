package com.example.producingwebservice.api;

import com.example.producingwebservice.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {

    User getCurrentUser();
    User save(User user);
}
