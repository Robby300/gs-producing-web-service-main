package com.example.producingwebservice.model;

import com.example.producingwebservice.type.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public class UserDto {
    private String username;
    private String password;
    private Set<Role> roles;
}
