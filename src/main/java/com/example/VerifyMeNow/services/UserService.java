package com.example.VerifyMeNow.services;
import com.example.VerifyMeNow.entity.User;

public interface UserService {
    User LoadByUsername(String username);


}
