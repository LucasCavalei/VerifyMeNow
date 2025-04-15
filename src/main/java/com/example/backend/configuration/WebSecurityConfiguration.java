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
                // Habilitar CORS usando a configuração definida no bean corsConfigurationSource()
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/auth/login").permitAll()
                                // Acesso à rota /helloworld
                                .requestMatchers("/auth/register").permitAll()
                                .anyRequest().authenticated() // Todas as outras rotas precisam de autenticação
                );
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

        }
    // --- Bean de Configuração CORS ---
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite requisições de qualquer origem.
        // ATENÇÃO: Para produção, é ALTAMENTE recomendado restringir a origens específicas!
        // Ex: configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://seudominiofrontend.com"));
        configuration.setAllowedOriginPatterns(List.of("*")); // Use allowedOriginPatterns com "*" se precisar de allowCredentials(true)

        // Permite os métodos HTTP especificados (GET, POST, PUT, DELETE, etc.)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));

        // Permite todos os cabeçalhos na requisição
        configuration.setAllowedHeaders(List.of("*")); // Ou especifique cabeçalhos: Arrays.asList("Authorization", "Cache-Control", "Content-Type")

        // Permite que o navegador envie credenciais (como cookies ou cabeçalhos de autenticação)
        // Necessário se seu frontend envia tokens JWT no cabeçalho Authorization
        configuration.setAllowCredentials(true);

        // Configura quais cabeçalhos podem ser expostos ao frontend (opcional, mas útil para Authorization)
        // configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuração a todas as rotas da sua API (/**)
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
