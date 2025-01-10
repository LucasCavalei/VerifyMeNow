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
public class    TokenAuthenticationFilter extends OncePerRequestFilter{
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private TokenProvider tokenProvider;
    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

   @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       String username;

       String path = request.getServletPath();

       // Ignore rotas públicas
       if (path.equals("/helloworld") || path.equals("/login") || path.equals("/register")) {
           filterChain.doFilter(request, response);
           return;
       }


       logger.info("Recebido o seguinte request do cliente {}", request);
       String jwt = tokenProvider.getJwtFromRequest(request);
       if (jwt != null) {
           try {

               username = tokenProvider.getUsernameFromToken(jwt);
               logger.info("recebido o seguinte authenticationfilter request com  username: {}", username);

               UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
               if (tokenProvider.validateToken(jwt, userDetails)) {
                   UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                           userDetails.getPassword());
                   SecurityContextHolder.getContext().setAuthentication(authentication);
               }
           } catch (Exception e) {
               logger.error("Não pode estabelecer autenticação: {}", e);
           }
           filterChain.doFilter(request, response);
       }
   }
}


