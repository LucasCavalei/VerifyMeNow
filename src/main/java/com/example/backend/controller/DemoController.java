package com.example.backend.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {


    @GetMapping("/helloworld")
    public String sayHello() {
        System.out.println("Olá, chegou no sayHalloMethod");
        return "helloworld";
    }
}

