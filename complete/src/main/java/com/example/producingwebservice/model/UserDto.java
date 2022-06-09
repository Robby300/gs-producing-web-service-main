package com.example.producingwebservice.model;

import com.example.producingwebservice.type.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Setter
@Getter
public class UserDto {
    private long id;
    private String username;
    private String password;
    private Set<Role> roles;
}
