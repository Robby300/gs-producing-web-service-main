package com.example.producingwebservice.repository;

import com.example.producingwebservice.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
