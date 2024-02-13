package com.example.VerifyMeNow.controller;

import com.example.VerifyMeNow.entity.User;
import com.example.VerifyMeNow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @PostMapping("/signup")

    public User saveUser(@RequestBody User user){
   return userService.saveUser(user);

    }
    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot!";
    }

    @PostMapping("/login")
    public String LoginUser(@RequestBody User user){

            return userService.authenticate(user);
    }
    @GetMapping("{id}")
    public Optional<User> getUserById(@PathVariable("id") Long id){
        return userService.getUserById(id);
    }
    @GetMapping("/")
    public List<User> getUsers(){
        return userService.findAll();
    }

}
