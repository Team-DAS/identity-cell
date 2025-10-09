package com.udeajobs.identity.account_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Account Service de UdeAJobs.
 *
 * Esta aplicación maneja la gestión de cuentas de usuario, incluyendo registro,
 * verificación de email, recuperación de contraseña y autenticación básica.
 * Forma parte del ecosistema de microservicios de UdeAJobs.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class AccountServiceApplication {

	/**
	 * Método principal que inicia la aplicación Spring Boot.
	 *
	 * @param args argumentos de línea de comandos pasados a la aplicación
	 */
	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}

}
