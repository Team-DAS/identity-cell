package com.udeajobs.identity.account_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Datos requeridos para verificar una cuenta de usuario")
public record VerificationRequest(
        @Schema(
                description = "Email del usuario que desea verificar su cuenta",
                example = "juan.perez@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Email is mandatory")
        String email,

        @Schema(
                description = "Código de verificación de 6 caracteres alfanuméricos enviado por email",
                example = "A1B2C3",
                requiredMode = Schema.RequiredMode.REQUIRED,
                pattern = "^[0-9a-zA-Z]{6}$",
                minLength = 6,
                maxLength = 6
        )
        @NotNull(message = "Code is mandatory")
        @Pattern(regexp = "^[0-9a-zA-Z]{6}$", message = "Code must be 6 characters long")
        String code
) {
}
