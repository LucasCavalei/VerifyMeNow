package com.example.backend.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.backend.security.TokenAuthenticationFilter;
import com.example.backend.services.imlp.CustomUserDetailsService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


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
    public SecurityFilterChain securityFilterChain(@org.jetbrains.annotations.NotNull HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/auth/login").permitAll()
                                .requestMatchers("/auth/register").permitAll()
                                .requestMatchers("/helloworld").permitAll() //
                                .requestMatchers("/api/home-data").authenticated()
                                .requestMatchers("/api/professor/dashboard-data").authenticated()
                                .anyRequest().authenticated() // Todas as outras rotas precisam de autenticação
                );
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

        }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Ex: configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://seudominiofrontend.com"));
        configuration.setAllowedOriginPatterns(List.of("*")); // Use allowedOriginPatterns com "*" se precisar de allowCredentials(true)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
        configuration.setAllowedHeaders(List.of("*")); // Ou especifique cabeçalhos: Arrays.asList("Authorization", "Cache-Control", "Content-Type")
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
