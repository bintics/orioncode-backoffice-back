package com.orioncode.backoffice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para APIs REST stateless
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) // Permitir consola H2 en frames
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos de documentación y H2 Console (en dev)
                .requestMatchers("/api/swagger-ui/**", "/swagger-ui/**", "/api/swagger-ui.html", "/swagger-ui.html",
                                 "/api/api-docs/**", "/api-docs/**", "/api/v3/api-docs/**", "/v3/api-docs/**",
                                 "/api/h2-console/**", "/h2-console/**").permitAll()
                
                // Endpoints públicos de MCP (si se requiere acceso libre)
                .requestMatchers("/api/mcp/**", "/mcp/**").permitAll()
                
                // El resto de la API requiere autenticación
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // Usar el claim "roles" inyectado por auth-service
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        // No añadir prefijo adicional porque los roles ya vienen con prefijo "ROLE_"
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
