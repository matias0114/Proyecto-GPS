package com.ProyectoGPS.Backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import static org.springframework.security.config.Customizer.withDefaults;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Habilita CORS usando el CorsConfigurationSource de más abajo
                .cors(withDefaults())
                // Deshabilita CSRF para API REST
                .csrf(csrf -> csrf.disable())
                // Configura las solicitudes OPTIONS para que no se bloqueen
                .authorizeRequests(auth -> auth
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // Permite solicitudes OPTIONS
                                .requestMatchers("/actuator/prometheus").permitAll()
                                .anyRequest().permitAll()
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowCredentials(true);

        // Cambié setAllowedOriginPatterns a setAllowedOrigins para mayor compatibilidad
        cfg.setAllowedOrigins(List.of(
            "http://190.13.177.173:8080",
            "http://190.13.177.173:85",
            "http://190.13.177.173:8005",
            "http://localhost:4200",
            "http://springboot-app:8080"
        ));

        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setExposedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica este CORS a todas las rutas
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
