package com.udeajobs.identity.account_service.dto;

import com.udeajobs.identity.account_service.enums.ROLE;
import jakarta.validation.constraints.*;

/**
 * DTO para las peticiones de registro de nuevos usuarios.
 *
 * Esta clase encapsula todos los datos necesarios para el registro de un nuevo usuario
 * en el sistema, incluyendo validaciones de formato para asegurar la integridad
 * de los datos y la seguridad de las contraseñas.
 *
 * @param fullName nombre completo del usuario (máximo 100 caracteres)
 * @param username nombre de usuario único para identificación
 * @param password contraseña con requisitos de seguridad (mínimo 8 caracteres, mayúscula, minúscula, número y carácter especial)
 * @param email dirección de correo electrónico válida
 * @param role rol del usuario en el sistema (FREELANCER o EMPLOYER)
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
public record RegistrationRequest(
    @NotBlank(message = "Fullname is mandatory")
    @Size(max = 100, message = "Fullname should be less than 100 characters")
    String fullName,
    @NotBlank(message = "Username is mandatory") String username,
    @NotBlank(message = "Password is mandatory")
        @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message =
                "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase"
                    + " letter, one number and one special character")
        String password,
    @NotBlank(message = "Email is mandatory") @Email(message = "Email should be valid") String email,
    @NotNull(message = "Role is mandatory") ROLE role) {}
