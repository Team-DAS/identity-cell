package com.udeajobs.identity.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * Configuración de OpenAPI 3.0 para la documentación Swagger UI.
 *
 * Esta clase configura la información general de la API, incluyendo
 * título, descripción, versión, información de contacto, licencia
 * y servidores disponibles para la documentación interactiva.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class OpenApiConfig {
    /**
     * Configura la documentación OpenAPI para el servicio de autenticación.
     *
     * Define toda la metadata de la API, incluyendo información general,
     * contacto del equipo, licencia y servidores disponibles.
     *
     * @return instancia de OpenAPI configurada con toda la metadata de la API
     */
    @Bean
    public OpenAPI authServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("UdeAJobs - Auth Service API")
                        .description("""
                                **API REST para la gestión de autenticación de usuarios en UdeAJobs**
                                
                                Este microservicio maneja la autenticación y autorización de usuarios, incluyendo:
                                
                                - 🔐 **Login de usuarios**: Autenticación con email y contraseña
                                - 🎫 **Generación de tokens JWT**: Emisión de access tokens y refresh tokens
                                - 🔄 **Renovación de tokens**: Actualización de access tokens mediante refresh tokens
                                - 👤 **Gestión de sesiones**: Manejo del ciclo de vida de tokens de acceso
                                
                                ### Seguridad
                                - Autenticación con Spring Security
                                - Tokens JWT con firma HMAC-SHA256
                                - Refresh tokens con expiración configurable
                                - Contraseñas encriptadas con BCrypt
                                
                                ### Tokens
                                - **Access Token**: Token de corta duración para acceso a recursos
                                - **Refresh Token**: Token de larga duración para renovar access tokens
                                
                                ### Base de datos
                                - MongoDB para almacenamiento NoSQL
                                - Colecciones: `user`, `refresh_tokens`
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("UdeAJobs Development Team")
                                .email("dev@udeajobs.com")
                                .url("https://github.com/udeajobs"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}

