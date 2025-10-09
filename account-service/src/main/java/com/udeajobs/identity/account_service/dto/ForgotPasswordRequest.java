package com.udeajobs.identity.account_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para las peticiones de recuperación de contraseña.
 *
 * Esta clase encapsula los datos necesarios para iniciar el proceso de
 * recuperación de contraseña, requiriendo únicamente el email del usuario
 * para enviar el enlace de recuperación.
 *
 * @param email dirección de correo electrónico válida del usuario
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
public record ForgotPasswordRequest(
        @NotBlank @Email String email
) {
}
