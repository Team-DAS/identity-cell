package com.udeajobs.identity.account_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para las peticiones de restablecimiento de contraseña.
 *
 * Esta clase encapsula los datos necesarios para completar el proceso de
 * restablecimiento de contraseña, incluyendo el token de verificación
 * y la nueva contraseña del usuario.
 *
 * @param token token único de verificación para autorizar el cambio de contraseña
 * @param newPassword nueva contraseña del usuario (mínimo 8 caracteres)
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@Schema(description = "Datos requeridos para restablecer la contraseña de un usuario")
public record ResetPasswordRequest(
        @Schema(
                description = "Token de restablecimiento único recibido por email",
                example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        String token,

        @Schema(
                description = "Nueva contraseña para la cuenta (mínimo 8 caracteres)",
                example = "NewP@ssw0rd",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minLength = 8
        )
        @NotBlank
        @Size(min = 8)
        String newPassword
) {
}
