package com.example.VerifyMeNow.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;


@Component
public class TokenProvider {
    //INSPIRADO POR PROJETO SECURE-USER-PLATFORM-VUE

    //public static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60 * (long) 8; // 8 Horas

    @Value("${app.jwtSecret}")
    private String secret;
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;
    private static final String JWT_PREFIX = "Bearer";
    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);


    public String createToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        String JWT = Jwts.builder()
                .setSubject(String.valueOf(username))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
        String token = JWT_PREFIX + " " + JWT;
        return token;
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        logger.info("Received request in getJWTfromRequest in tokenProvider {}", request);
        String bearerToken = request.getHeader("Authorization");
        logger.info("------------000------- Received bearerToken {}", bearerToken);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;

    }

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) { 
            username = null;
        }
        return username;
        // In this example, claims.getSubject() retrieves the value of the sub claim from the JWT payload,
        // which typically represents the username or user identifier
    }
    public boolean validateToken(String token, UserDetails userDetails) {
       // User user = (User) userDetails;
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    private boolean isTokenExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }
    private Date getExpirationDateFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.getExpiration();
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
//        In the context of JWT (JSON Web Tokens)
//        a Claims object represents the payload of the token. The payload contains claims,
//                -sub (Subject): Identifies the subject of the JWT. It typically contains the user ID or a unique identifier for the entity (user).
//                -iss (Issuer): Identifies the issuer of the JWT. It indicates who issued the token.
//                - iat (Issued At): Indicates the timestamp when the JWT was issued.
//                -aud (Audience): Identifies the audience for which the JWT is intended. It specifies who or what the intended audience is.
//                -exp (Expiration Time): Indicates the expiration time of the JWT. After this time, the token should not be accepted.
//        Claims interface provides a method called getExpiration() that corresponds to the exp claim
//        The value of the exp claim is typically a NumericDate,
//        representing the expiration time as the number of seconds since 1970-01-01 00:00:00 UTC.
    }

}
