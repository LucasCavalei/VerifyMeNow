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
    //Classe TokenPorvider inspirada por projeto  SECURE-USER-PLATFORM-VUE

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
        //logger.info("Received request in getJWTfromRequest in tokenProvider {}", request);
        String bearerToken = request.getHeader("Authorization");
        logger.info(" O bearerToken é  {}", bearerToken);

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

        //Neste exemplo, claims.getSubject() recupera o valor da declaração sub do payload do JWT,
        // que normalmente representa o nome de usuário ou o identificador do usuário.
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
        //     sub (Subject): Identifica o assunto do JWT. Geralmente contém o ID do usuário ou um identificador único para a entidade (usuário).
        //iss (Issuer): Identifica o emissor do JWT. Indica quem emitiu o token.
        //iat (Issued At): Indica o timestamp (data e hora) em que o JWT foi emitido.
        //aud (Audience): Identifica a audiência para a qual o JWT é destinado. Especifica quem ou o que é o público-alvo do token.
        //exp (Expiration Time): Indica o tempo de expiração do JWT. Após esse horário, o token não deve ser aceito.
        //A interface Claims fornece um método chamado getExpiration() que corresponde à declaração exp.
        //O valor da declaração exp geralmente é um NumericDate, que representa o horário de expiração como o número de segundos desde 01-01-1970 00:00:00 UTC.
            }

}
