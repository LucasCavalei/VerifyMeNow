package com.example.VerifyMeNow.configuration;

import com.example.VerifyMeNow.security.TokenAuthenticationFilter;
import com.example.VerifyMeNow.services.imlp.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
 @EnableWebSecurity
@EnableMethodSecurity
@PropertySource(value = "classpath:application.properties")
public class WebSecurityConfiguration {


     @Autowired
     TokenAuthenticationFilter tokenAuthenticationFilter;
    @Autowired
    CustomUserDetailsService jwtUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Desativa o CSRF (recomendado apenas para APIs sem sessão, como JWT)
        http.csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(HttpMethod.POST, "/login").permitAll() // Login livre
                                .requestMatchers(HttpMethod.GET, "/helloworld").permitAll() // Acesso à rota /helloworld
                                .requestMatchers(HttpMethod.POST, "/register").permitAll() // Registro livre (GET)
                                .anyRequest().authenticated() // Todas as outras rotas precisam de autenticação
                );
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
   }
}
