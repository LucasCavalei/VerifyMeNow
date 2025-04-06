package com.example.backend.services.imlp;

import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backend.entity.User;
import com.example.backend.exception.UserRegistrationException;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.TokenProvider;
import com.example.backend.services.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;


    @Override
    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }
    @Override
    public User findByUsername(String username){
        User userFound = userRepository.findByUsername(username);
        return userFound;
    }
    @Override
    public User saveUser(User user){


        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getEmail()) || StringUtils.isBlank(user.getPassword())) {
            throw new UserRegistrationException("Nome, email e senha não podem estar vazios ");
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserRegistrationException("Nome do usuario já existe");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserRegistrationException("Email já existe");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
        //return tokenProvider.createToken(savedUser);
    }
    @Override
    public String authenticate(User user){
        log.info("Usuario recebido no metotodo Authenticate antes da autenticação: {}", user);
        try {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.info("Este é UserDetails depois da autenticação", userDetails);

        System.out.println("User " + userDetails.getUsername() + " Usuario logado com sucesso.");
                  //FALTA
         //Inject into security context
		SecurityContextHolder.getContext().setAuthentication(authentication);
        String username = userDetails.getUsername();

        //User user = (User) authentication.getPrincipal();
        //log.info("userTemporaryname Received login request with user data: {}",userDetails);
String token = tokenProvider.createToken(username);

return token;
        } catch (UsernameNotFoundException e) {
            // Handle username not found exception
            log.error("Username not found: {}", user.getUsername(), e);
            throw new UsernameNotFoundException("Username not found", e);
        }

    }

    @Override
    public List<User> findAll(){
       return userRepository.findAll();
    }
}
