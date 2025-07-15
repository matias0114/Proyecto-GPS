package com.ProyectoGPS.Backend;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/prometheus").permitAll()
                .anyRequest().permitAll()
            )
            .httpBasic();
        return http.build();
    }

    // Esta configuración se usa para el filtro manual de CORS
    private CorsConfiguration buildCorsConfig() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowCredentials(true);
        cfg.setAllowedOriginPatterns(List.of("*")); // Acepta todos los orígenes (útil en red interna)
        cfg.addAllowedHeader("*");
        cfg.addAllowedMethod("*");
        cfg.addExposedHeader("Authorization");
        cfg.addExposedHeader("Content-Type");
        cfg.setMaxAge(3600L);
        return cfg;
    }

    // Filtro con prioridad alta que se aplica antes que Spring Security
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildCorsConfig());

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE); // Prioridad máxima
        return bean;
    }
}
