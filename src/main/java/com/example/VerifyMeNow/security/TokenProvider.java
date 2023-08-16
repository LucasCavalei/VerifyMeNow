package com.example.VerifyMeNow.security;

import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import java.util.Date;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.file.attribute.UserPrincipal;

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

}


