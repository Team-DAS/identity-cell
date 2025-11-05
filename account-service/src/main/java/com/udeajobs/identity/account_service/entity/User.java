package com.udeajobs.identity.account_service.entity;

import com.udeajobs.identity.account_service.enums.ROLE;
import com.udeajobs.identity.account_service.enums.STATUS;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Entidad que representa un usuario en el sistema UdeAJobs.
 *
 * Esta clase mapea la información básica de un usuario, incluyendo datos
 * de identificación, autenticación y estado. Se almacena en MongoDB
 * y maneja tanto el proceso de registro como la recuperación de contraseñas.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@Schema(description = "Entidad que representa un usuario registrado en el sistema")
@Data
@Document(collection = "user")
@Builder
public class User {

    /** Identificador único del usuario en la base de datos */
    @Schema(
            description = "Identificador único del usuario en MongoDB",
            example = "507f1f77bcf86cd799439011",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Id
    private String id;

    /** Nombre completo del usuario */
    @Schema(
            description = "Nombre completo del usuario",
            example = "Juan Carlos Pérez"
    )
    private String fullName;

    /** Nombre de usuario único para autenticación */
    @Schema(
            description = "Nombre de usuario único",
            example = "jcperez"
    )
    private String username;

    /** Contraseña encriptada del usuario */
    @Schema(
            description = "Contraseña encriptada del usuario",
            accessMode = Schema.AccessMode.WRITE_ONLY,
            hidden = true
    )
    private String password;

    /** Dirección de correo electrónico del usuario (única) */
    @Schema(
            description = "Dirección de correo electrónico única del usuario",
            example = "juan.perez@example.com"
    )
    private String email;

    /** Rol del usuario en el sistema (FREELANCER o EMPLOYER) */
    @Schema(
            description = "Rol del usuario en el sistema",
            example = "FREELANCER",
            allowableValues = {"FREELANCER", "EMPLOYER"}
    )
    private ROLE role;

    /** Estado actual del usuario (PENDING_VERIFICATION, ACTIVE, INACTIVE) */
    @Schema(
            description = "Estado actual de la cuenta del usuario",
            example = "PENDING_VERIFICATION",
            allowableValues = {"PENDING_VERIFICATION", "ACTIVE", "INACTIVE"}
    )
    private STATUS status;

    /** Código de verificación temporal para activación de cuenta */
    @Schema(
            description = "Código de verificación de 6 caracteres para activar la cuenta",
            example = "A1B2C3",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private String verificationCode;

    /** Token temporal para recuperación de contraseña */
    @Schema(
            description = "Token único para recuperación de contraseña",
            accessMode = Schema.AccessMode.READ_ONLY,
            hidden = true
    )
    private String resetPasswordToken;

    /** Fecha y hora de expiración del token de recuperación de contraseña */
    @Schema(
            description = "Fecha y hora de expiración del token de recuperación",
            example = "2025-11-05T15:30:00",
            accessMode = Schema.AccessMode.READ_ONLY,
            hidden = true
    )
    private LocalDateTime resetPasswordTokenExpiration;
}
