package com.udeajobs.identity.account_service.dto;

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
public record ResetPasswordRequest(
        @NotBlank String token,
        @NotBlank @Size(min = 8) String newPassword
) {
}
