package com.udeajobs.identity.account_service.config;

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
     * Configura la documentación OpenAPI para el servicio de cuentas.
     *
     * Define toda la metadata de la API, incluyendo información general,
     * contacto del equipo, licencia y servidores disponibles.
     *
     * @return instancia de OpenAPI configurada con toda la metadata de la API
     */
    @Bean
    public OpenAPI accountServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("UdeAJobs - Account Service API")
                        .description("""
                                **API REST para la gestión de cuentas de usuario en UdeAJobs**
                                
                                Este microservicio maneja el ciclo de vida completo de las cuentas de usuario, incluyendo:
                                
                                - ✅ **Registro de usuarios**: Creación de cuentas para freelancers y employers
                                - 📧 **Verificación por email**: Confirmación de cuentas mediante código de 6 caracteres
                                - 🔐 **Recuperación de contraseña**: Proceso seguro de restablecimiento vía email
                                - 🔄 **Restablecimiento de contraseña**: Actualización de contraseña con token temporal
                                
                                ### Seguridad
                                - Contraseñas encriptadas con BCrypt
                                - Tokens de recuperación con expiración temporal
                                - Validación de datos con Bean Validation (JSR-303)
                                
                                ### Notificaciones
                                - Emails transaccionales con plantillas Thymeleaf
                                - Integración con servicio de mensajería SMTP
                                
                                ### Base de datos
                                - MongoDB para almacenamiento NoSQL
                                - Colección: `user`
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("UdeAJobs Development Team")
                                .email("udeajobs674@gmail.com")
                                .url("https://github.com/Team-DAS"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}

