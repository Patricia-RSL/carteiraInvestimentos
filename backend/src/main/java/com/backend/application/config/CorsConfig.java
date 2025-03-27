package com.backend.application.config;  // Coloque o pacote de acordo com o seu projeto

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");  // Permite todas as origens. (Pode ser restritivo em produção)
        config.addAllowedMethod("*");  // Permite todos os métodos HTTP (GET, POST, etc.)
        config.addAllowedHeader("*");  // Permite todos os headers
        config.setAllowCredentials(false); // Permite cookies se necessário

        // Configura a aplicação para aceitar CORS em todas as rotas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}