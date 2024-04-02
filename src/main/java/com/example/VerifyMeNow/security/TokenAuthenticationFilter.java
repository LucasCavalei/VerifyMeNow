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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter{
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private TokenProvider tokenProvider;
    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);



   @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      String username;

   //public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
       try {
           logger.info("Received request for username in Authenticationfilter {}", request);

       String jwt = tokenProvider.getJwtFromRequest(request);
           logger.info("jwt from request for username in Authenticationfilter {}", jwt);

       username = tokenProvider.getUsernameFromToken(jwt);
           logger.info("Received username in Authenticationfilter request with user data: {}", username);

       UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
       if (tokenProvider.validateToken(jwt, userDetails)) {
           UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                   userDetails.getPassword());
           SecurityContextHolder.getContext().setAuthentication(authentication);
       }
       } catch (Exception e) {
           logger.error("Cannot set user authentication: {}", e);
       }
       filterChain.doFilter(request, response);
   }

}


