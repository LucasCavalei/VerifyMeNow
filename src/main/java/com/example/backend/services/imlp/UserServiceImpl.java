package com.example.backend.services.imlp;
import com.example.backend.entity.ERole;
import com.example.backend.entity.Role;
import com.example.backend.repository.RoleRepository;
import com.example.backend.request.LoginRequest;
import com.example.backend.request.SignupRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
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
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    PasswordEncoder encoder;
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
    public ResponseEntity<?> saveUser(@Valid SignupRequest signupRequest){

        if (userRepository.findByUsername(signupRequest.getUsername()) != null) {
            throw new UserRegistrationException("Nome do usuario já existe");
        }
        if (userRepository.findByEmail(signupRequest.getEmail()) != null) {
            throw new UserRegistrationException("Email já existe");
        }
        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role não encontrado."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if (role.equals("admin")) {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role não encontrado."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role não encontrado."));
                    roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
       return ResponseEntity.ok("Usuario criado com sucesso!");
    }


    @Override
    public ResponseEntity<?> authenticate(@Valid LoginRequest loginRequest){
        log.info("chegou em authenticate", loginRequest.getUsername());

        try {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.info("Este é UserDetails depois da autenticação", userDetails);

        System.out.println("User " + userDetails.getUsername() + " Usuario logado com sucesso.");
                  //FALTA
         //Inject into security context
		SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(userDetails);

            Map<String, String> resposta = new HashMap<>();
            resposta.put("token", token);
            return ResponseEntity.ok(resposta);

        } catch (UsernameNotFoundException e) {
            // Handle username not found exception
            log.error("Username not found: {}", loginRequest.getUsername(), e);
            throw new UsernameNotFoundException("Username not found", e);
        }
    }

    @Override
    public List<User> findAll(){
       return userRepository.findAll();
    }
}
