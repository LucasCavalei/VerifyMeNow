package com.example.VerifyMeNow.services;
import com.example.VerifyMeNow.entity.User;

import java.util.Optional;

public interface UserService {
    User findByUsername(String username);
    User saveUser(User user);
    Optional<User> getUser(Long id);


}

