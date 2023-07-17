package com.example.VerifyMeNow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/")
    public String retorneAlgo(){
        return "Hello world";
    }
}
