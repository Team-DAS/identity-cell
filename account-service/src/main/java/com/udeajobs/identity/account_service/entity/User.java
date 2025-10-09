package com.udeajobs.identity.account_service.entity;

import com.udeajobs.identity.account_service.enums.ROLE;
import com.udeajobs.identity.account_service.enums.STATUS;
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
@Data
@Document(collection = "user")
@Builder
public class User {

    /** Identificador único del usuario en la base de datos */
    @Id
    private String id;

    /** Nombre completo del usuario */
    private String fullName;

    /** Nombre de usuario único para autenticación */
    private String username;

    /** Contraseña encriptada del usuario */
    private String password;

    /** Dirección de correo electrónico del usuario (única) */
    private String email;

    /** Rol del usuario en el sistema (FREELANCER o EMPLOYER) */
    private ROLE role;

    /** Estado actual del usuario (PENDING_VERIFICATION, ACTIVE, INACTIVE) */
    private STATUS status;

    /** Código de verificación temporal para activación de cuenta */
    private String verificationCode;

    /** Token temporal para recuperación de contraseña */
    private String resetPasswordToken;

    /** Fecha y hora de expiración del token de recuperación de contraseña */
    private LocalDateTime resetPasswordTokenExpiration;
}
