package com.sofka.bingo.utility;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de la aplicación web.
 * @version 1.0.000 2023-02-28
 * @author Wendy Arcila
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Agrega la configuración para permitir CORS en la aplicación web.
     * @param registry el registro para configurar CORS
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*");
    }
}
