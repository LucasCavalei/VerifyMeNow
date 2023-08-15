package com.example.VerifyMeNow.controller;

import com.example.VerifyMeNow.entity.User;
import com.example.VerifyMeNow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/signup")
    public User saveUser(@RequestBody User user){
    //adicionar se user exist
        return userService.saveUser(user);
    }

    @PostMapping
    public User LoginUser(@RequestBody User user){
        return userService.authenticate(user);
    }
    @GetMapping("{id}")
    public Optional<User> getUser(@PathVariable("id") Long id){

        return userService.getUser(id);
    }
    @GetMapping("/")
    public List<User> getUsers(){
        return userService.findAll();
    }

}
