package com.udeajobs.identity.auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Auth Service de UdeAJobs.
 *
 * Este microservicio gestiona la autenticación de usuarios mediante tokens JWT,
 * proporcionando endpoints para login y renovación de tokens.
 *
 * <p>Características principales:
 * <ul>
 *     <li>Autenticación con Spring Security</li>
 *     <li>Generación de JWT tokens</li>
 *     <li>Refresh tokens para renovación de acceso</li>
 *     <li>Integración con MongoDB</li>
 *     <li>Documentación con OpenAPI/Swagger</li>
 * </ul>
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class AuthServiceApplication {

	/**
	 * Punto de entrada de la aplicación Spring Boot.
	 *
	 * @param args argumentos de línea de comandos
	 */
	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
