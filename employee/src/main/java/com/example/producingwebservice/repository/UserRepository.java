package com.example.producingwebservice.repository;


import com.example.producingwebservice.entity.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
	Optional<User> findByUsername(String username);
}
