package com.example.backend.services;

import com.example.backend.entity.User;
import com.example.backend.request.LoginRequest;
import com.example.backend.request.SignupRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User findByUsername(String username);
    ResponseEntity<?> saveUser(@Valid SignupRequest user);      // antes: User saveUser
    Optional<User> getUserById(Long id);
    List<User> findAll();
    ResponseEntity<?> authenticate(@Valid LoginRequest user);  // antes: String authenticate
}