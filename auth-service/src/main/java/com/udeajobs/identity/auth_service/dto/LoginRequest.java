package com.udeajobs.identity.auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para la solicitud de autenticación de usuario.
 *
 * Contiene las credenciales necesarias para el login: email y contraseña.
 *
 * @param email correo electrónico del usuario (no puede estar vacío)
 * @param password contraseña del usuario (no puede estar vacía)
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@Schema(description = "Solicitud de login con credenciales de usuario")
public record LoginRequest(
        @Schema(description = "Correo electrónico del usuario", example = "usuario@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Email cannot be blank")
         String email,
        @Schema(description = "Contraseña del usuario", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Password cannot be blank")
         String password
) {
}
