package com.example.producingwebservice.model;

import com.example.producingwebservice.type.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Setter
@Getter
@RequiredArgsConstructor
@ToString(exclude = {"id"})
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Set<Role> roles;
}
