package com.example.VerifyMeNow.services.imlp;
import com.example.VerifyMeNow.entity.User;
import com.example.VerifyMeNow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.loadUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario não encontrado ", null);

        }
        return null;
    }
}