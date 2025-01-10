package com.example.VerifyMeNow.controller;

import com.example.VerifyMeNow.entity.User;
import com.example.VerifyMeNow.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired

    private UserService userService;

    @PostMapping("/register")
    public User SaveUser(@RequestBody User user) {

        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public String LoginUser(@RequestBody User user){
            return userService.authenticate(user);
    }

    @GetMapping("{id}")
    public Optional<User> getUserById(@PathVariable("id") Long id){
        return userService.getUserById(id);
    }

   // @GetMapping("/")
   // public List<User> getUsers(){
     //   return userService.findAll();
    //}


   // @GetMapping("/helloworld")
   // @ResponseBody
   // public String sayHello() {

     //   return "helloworld";
    //}
}
