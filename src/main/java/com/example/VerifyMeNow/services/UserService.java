package com.example.VerifyMeNow.services;
import com.example.VerifyMeNow.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User findByUsername(String username);
    User saveUser(User user);
    Optional<User> getUserById(Long id);
    List<User> findAll();
    String authenticate(User user);



}

