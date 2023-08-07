package com.example.VerifyMeNow.controller;

import com.example.VerifyMeNow.entity.User;
import com.example.VerifyMeNow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public User saveUser(@RequestBody User user){
        return userService.saveUser(user);
    }
    @GetMapping("{id}")
    public Optional<User> getUser(@PathVariable("id") Long id){
        return userService.getUser(id);
    }
}
