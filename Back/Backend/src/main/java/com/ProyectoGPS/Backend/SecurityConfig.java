package com.ProyectoGPS.Backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;
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
            .cors().and() // habilita CORS
            .csrf().disable() // deshabilita CSRF para API REST
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/prometheus").permitAll()
                .anyRequest().permitAll()
            )
            .httpBasic(); // autenticación básica (puedes cambiarlo después)
        return http.build();
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowCredentials(true);

        // Acepta orígenes desde localhost, contenedores, red local y navegadores
        cfg.setAllowedOriginPatterns(List.of(
            "http://localhost:*",
            "http://gps-backend:*",
            "http://190.13.177.173:*"
        ));

        cfg.addAllowedHeader("*");
        cfg.addAllowedMethod("*");
        cfg.addExposedHeader("Authorization");
        cfg.addExposedHeader("Content-Type");
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE); // Muy importante
        return bean;
    }
}
