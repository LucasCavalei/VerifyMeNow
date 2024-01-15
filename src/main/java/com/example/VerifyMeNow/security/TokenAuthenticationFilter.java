package com.example.VerifyMeNow.security;

import com.example.VerifyMeNow.services.imlp.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class TokenAuthenticationFilter extends OncePerRequestFilter{
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private TokenProvider tokenProvider;
    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);


   //Por que este @Override
//   Your TokenAuthenticationFilter class extends OncePerRequestFilter.
//    The OncePerRequestFilter class is part of the Spring Security framework
//        and provides a convenient base class for filters that should be applied only once per request.
//    OncePerRequestFilter has a method named doFilterInternal that
//    you can override to provide your own logic for handling requests.
//    By adding @Override before the doFilterInternal method in your
//        TokenAuthenticationFilter class, you are explicitly telling the compiler that you intend
//    to provide your own implementation for this method, and it is expected to override the same method in
//    the superclass (OncePerRequestFilter).
   @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      String username;

   //public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


       //função logo abaixo
       String jwt = tokenProvider.getJwtFromRequest(request);
       username = tokenProvider.getUsernameFromToken(jwt);
       UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
       if (tokenProvider.validateToken(jwt, userDetails)) {
           UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                   userDetails.getPassword());
           SecurityContextHolder.getContext().setAuthentication(authentication);
       }
       filterChain.doFilter(request, response);
   }

}


