package com.udeajobs.identity.account_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * DTO para las peticiones de verificación de cuenta.
 *
 * Esta clase encapsula los datos necesarios para verificar una cuenta de usuario,
 * incluyendo el email y el código de verificación. Incluye validaciones para
 * asegurar que los datos están en el formato correcto.
 *
 * @param email dirección de correo electrónico del usuario a verificar
 * @param code código de verificación de 6 caracteres alfanuméricos
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
public record VerificationRequest(
        @NotNull(message = "Email is mandatory")
        String email,
        @NotNull(message = "Code is mandatory")
        @Pattern(regexp = "^[0-9a-zA-Z]{6}$", message = "Code must be 6 characters long")
        String code
) {
}
