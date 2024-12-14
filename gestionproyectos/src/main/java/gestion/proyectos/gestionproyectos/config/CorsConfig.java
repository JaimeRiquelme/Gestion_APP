package gestion.proyectos.gestionproyectos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Permite todos los orígenes - en producción deberías especificar los orígenes permitidos
        config.addAllowedOrigin("*");

        // Permite las credenciales
        config.setAllowCredentials(true);

        // Permite todos los headers
        config.addAllowedHeader("*");

        // Permite todos los métodos HTTP (GET, POST, etc.)
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}