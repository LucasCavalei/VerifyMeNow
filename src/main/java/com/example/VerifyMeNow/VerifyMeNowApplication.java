package com.example.VerifyMeNow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Array;
import java.util.ArrayList;

@SpringBootApplication
public class VerifyMeNowApplication {

	public static void main(String[] args) {
		SpringApplication.run(VerifyMeNowApplication.class, args);
		String petName ="Rick";
		Integer idade = 2;
		System.out.println("o meu cachorro chama se"+ petName + "ele tem " + idade + "anos de idade.");
		//Array<String> names = new ArrayList<>();
		String[] names = new String[3];
		names[0] = "lucas";
		names[1] = "rafael";
		names[2] =  "junho";
   for(int i=0;i<3;i++){
		System.out.println(names[i]); }
  }
}