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


//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(jwtUserDetailsService)
//                .passwordEncoder(passwordEncoder());
//    }

    // talvez peça authetication povider com userDetails serve and passwordEncorder
    // este é moderno e pede -> https://www.youtube.com/watch?v=NIv9TFTSIlg
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
               // .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(HttpMethod.POST,"/signup").permitAll()
                                .requestMatchers(HttpMethod.POST,"/login").permitAll()
                                .requestMatchers(HttpMethod.GET,"/hello").permitAll()
                                .anyRequest().authenticated()
                );

     //  http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

//        http.csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(req ->
//                        req.requestMatchers("/login","/signup")
//                                .permitAll()
//                                .anyRequest()
//                                .authenticated()
//                )
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//
//
//        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
   }
//    @Bean
//    public TokenAuthenticationFilter tokenAuthenticationFilter() {
//        return new TokenAuthenticationFilter();
//
//    }
}

