package com.example.backend.services;

import com.example.backend.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User findByUsername(String username);
    ResponseEntity<?> saveUser(User user);      // antes: User saveUser
    Optional<User> getUserById(Long id);
    List<User> findAll();
    ResponseEntity<?> authenticate(User user);  // antes: String authenticate
}