package com.example.VerifyMeNow.services.imlp;
import com.example.VerifyMeNow.entity.User;
import com.example.VerifyMeNow.repository.UserRepository;
import com.example.VerifyMeNow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public Optional<User> getUser(Long id){
        return userRepository.findById(id);
    }
    @Override
    public User findByUsername(String username){
        User user = userRepository.findByUsername(username);
        return user;
    }
    @Override
    public User saveUser(User user){
        //User user = findByUsername(user.getUsername());
        //if user{
          //  return "Usuario ja existe";
        //}
        return userRepository.save(user);
    }
}
