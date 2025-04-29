package com.example.backend.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class TokenProvider {
    //Classe TokenPorvider inspirada por projeto  SECURE-USER-PLATFORM-VUE

    @Value("${app.jwtSecret}")
    private String secret;
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;
    private static final String JWT_PREFIX = "Bearer";
    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);


    public String createToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // <- Aqui

        String jwt = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(role -> role.getAuthority())
                        .toList())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256) // <- E aqui
                .compact();

        return JWT_PREFIX + " " + jwt;
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        //logger.info("Received request in getJWTfromRequest in tokenProvider {}", request);
        String bearerToken = request.getHeader("Authorization");
        logger.info(" O bearerToken é  {}", bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // Extrai a parte depois de "Bearer "
            String jwt = bearerToken.substring(7);

            // Log para ver o token logo após o substring
            logger.debug("Token after substring(7): '[{}]'", jwt); // Coloquei entre [] para ver espaços

            // *** APLICA O TRIM AQUI ***
            String trimmedJwt = jwt.trim();

            // Log para ver o token após o trim
            logger.debug("Token after trim(): '[{}]'", trimmedJwt);

            // Retorna o token limpo
            return trimmedJwt;
        }
        logger.debug("Authorization header missing, empty, or not starting with 'Bearer '.");
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
        logger.info(" userDeails em validate token  {}",userDetails.getUsername());

        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    public List<String> getRolesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        if (claims == null) return new ArrayList<>();
        return claims.get("roles", List.class);
    }


    private boolean isTokenExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }
    private Date getExpirationDateFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.getExpiration();
    }


         //------------------------------------------------------\\
         private Claims getAllClaimsFromToken(String token) {
             Claims claims;
             // Log para ver o que chega neste método
             logger.debug("Token received in getAllClaimsFromToken: '[{}]'", token);

             if (token == null) {
                 logger.warn("Token received in getAllClaimsFromToken is null.");
                 return null;
             }

             // Manter o trim aqui também é uma boa prática (defesa em profundidade)
             String trimmedToken = token.trim();
             if (trimmedToken.isEmpty()) {
                 logger.warn("Token received in getAllClaimsFromToken is empty after trim.");
                 return null;
             }
             // Log para confirmar o trim interno
             logger.debug("Token after trim() inside getAllClaimsFromToken: '[{}]'", trimmedToken);


             try {
                 Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                 claims = Jwts.parserBuilder()
                         .setSigningKey(key)
                         .build()
                         .parseClaimsJws(trimmedToken) // Use o token trimado
                         .getBody();
                 // ... (resto do método com os catches)
             } catch (SignatureException ex) {
                 logger.error("Assinatura JWT inválida: {}", ex.getMessage());
                 claims = null;
             } catch (MalformedJwtException ex) {
                 // O erro "Illegal base64url character" geralmente cai aqui se não for pego antes
                 logger.error("Token JWT malformado ou com caracteres inválidos: {}", ex.getMessage());
                 claims = null;
             } catch (ExpiredJwtException ex) {
                 logger.error("Token JWT expirado: {}", ex.getMessage());
                 claims = null;
             } catch (UnsupportedJwtException ex) {
                 logger.error("Token JWT não suportado: {}", ex.getMessage());
                 claims = null;
             } catch (IllegalArgumentException ex) {
                 logger.error("Argumento JWT inválido (vazio, nulo ou problema interno): {}", ex.getMessage());
                 claims = null;
             } catch (Exception e) {
                 logger.error("Erro inesperado ao parsear/validar token: {}", e.getMessage(), e);
                 claims = null;
             }

             logger.debug("Claims result: {}", claims);
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
