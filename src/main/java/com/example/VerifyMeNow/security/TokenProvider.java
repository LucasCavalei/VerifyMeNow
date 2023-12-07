package com.example.VerifyMeNow.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import java.util.Date;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.nio.file.attribute.UserPrincipal;


@Component
public class TokenProvider {
    private static final String JWT_SECRET_KEY = "TExBVkVfTVVZX1NFQ1JFVEE=";
    public static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60 * (long) 8; // 8 Horas

    
    public String createToken(Authentication authentication){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String username = userPrincipal.getName();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey( JWT_SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
          //  throw new CustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
            System.out.println("por aqui a execção e ecluir o retrun false");
        return false;
        }
    }
}


