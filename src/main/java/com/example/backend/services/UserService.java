package com.example.backend.services;
import java.util.List;
import java.util.Optional;

import com.example.backend.entity.User;

public interface UserService {
    User findByUsername(String username);
    User saveUser(User user);
    Optional<User> getUserById(Long id);
    List<User> findAll();
    String authenticate(User user);



}

