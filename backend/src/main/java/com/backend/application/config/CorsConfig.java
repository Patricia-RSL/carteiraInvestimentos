package com.backend.application.config;  // Coloque o pacote de acordo com o seu projeto

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
  @Value("${spring.web.cors.allowed-origins}")
  private String allowedOrigins;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin(this.allowedOrigins);
        config.addAllowedMethod("*");  // Permite todos os métodos HTTP (GET, POST, etc.)
        config.addAllowedHeader("*");  // Permite todos os headers
        config.setAllowCredentials(true);

        // Configura a aplicação para aceitar CORS em todas as rotas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
