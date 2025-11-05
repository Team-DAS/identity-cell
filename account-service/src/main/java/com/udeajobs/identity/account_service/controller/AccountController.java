package com.udeajobs.identity.account_service.controller;


import com.udeajobs.identity.account_service.dto.ErrorResponse;
import com.udeajobs.identity.account_service.dto.RegistrationRequest;
import com.udeajobs.identity.account_service.dto.ResetPasswordRequest;
import com.udeajobs.identity.account_service.dto.VerificationRequest;
import com.udeajobs.identity.account_service.dto.ForgotPasswordRequest;
import com.udeajobs.identity.account_service.entity.User;
import com.udeajobs.identity.account_service.service.interfaces.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para operaciones de gestión de cuentas de usuario.
 *
 * Este controlador expone endpoints para el registro de usuarios, verificación
 * de cuentas, recuperación de contraseñas y restablecimiento de contraseñas.
 * Maneja todas las operaciones relacionadas con el ciclo de vida de las cuentas.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@Tag(name = "Account Management", description = "APIs para gestión del ciclo de vida de cuentas de usuario - registro, verificación, recuperación de contraseña")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    /**
     * Registra una nueva cuenta de usuario en el sistema.
     *
     * Crea un nuevo usuario con los datos proporcionados, encripta la contraseña,
     * genera un código de verificación y envía un email de confirmación.
     *
     * @param registrationRequest datos necesarios para el registro del usuario
     * @return ResponseEntity con el usuario creado y código HTTP 201 (Created)
     */
    @Operation(
            summary = "Registrar nueva cuenta de usuario",
            description = "Crea una nueva cuenta de usuario en el sistema. Valida los datos, encripta la contraseña, " +
                    "genera un código de verificación de 6 caracteres y envía un email de confirmación. " +
                    "El usuario queda en estado PENDING_VERIFICATION hasta completar la verificación."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario registrado exitosamente. Se envió email de verificación.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de registro inválidos (formato de email, contraseña débil, campos obligatorios faltantes)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "El email o username ya está registrado en el sistema",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor al procesar el registro o enviar el email",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/register")
    public ResponseEntity<User> registerAccount(@Valid @RequestBody RegistrationRequest registrationRequest) {
        log.info("Iniciando registro de usuario con email: {}", registrationRequest.email());
        User user = User.builder()
                .fullName(registrationRequest.fullName())
                .username(registrationRequest.username())
                .email(registrationRequest.email())
                .password(registrationRequest.password())
                .role(registrationRequest.role())
                .build();
        User registeredUser = accountService.registerUser(user);
        log.info("Usuario registrado exitosamente con ID: {}", registeredUser.getId());
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    /**
     * Verifica una cuenta de usuario utilizando el código de verificación.
     *
     * Valida el código de verificación enviado por email y activa la cuenta
     * del usuario si el código es correcto.
     *
     * @param verificationRequest datos de verificación (email y código)
     * @return ResponseEntity con mensaje de éxito y código HTTP 200 (OK)
     */
    @Operation(
            summary = "Verificar cuenta de usuario",
            description = "Verifica la dirección de email del usuario mediante el código de verificación de 6 caracteres " +
                    "enviado por correo. Una vez verificado, el estado del usuario cambia de PENDING_VERIFICATION a ACTIVE " +
                    "y puede acceder completamente al sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cuenta verificada exitosamente. Usuario activado.",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(implementation = String.class, example = "User verified successfully")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Código de verificación inválido o formato incorrecto",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado con el email proporcionado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor al verificar la cuenta",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/verify")
    public ResponseEntity<String> verifyAccount(@Valid @RequestBody VerificationRequest verificationRequest) {
        log.info("Verificando cuenta para email: {}", verificationRequest.email());
        accountService.verifyUser(verificationRequest.email(), verificationRequest.code());
        log.info("Cuenta verificada exitosamente para email: {}", verificationRequest.email());
        return new ResponseEntity<>("User verified successfully",HttpStatus.OK);
    }

    /**
     * Inicia el proceso de recuperación de contraseña.
     *
     * Genera un token de recuperación y envía un email con el enlace
     * para restablecer la contraseña del usuario.
     *
     * @param forgotPasswordRequest datos para recuperación (email del usuario)
     * @return ResponseEntity con mensaje de confirmación y código HTTP 200 (OK)
     */
    @Operation(
            summary = "Iniciar recuperación de contraseña",
            description = "Inicia el proceso de recuperación de contraseña para un usuario. Genera un token único " +
                    "de restablecimiento con validez temporal y envía un email con el enlace para resetear la contraseña. " +
                    "El token expira después de un período determinado por seguridad."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Email de recuperación enviado exitosamente con el token de restablecimiento",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(implementation = String.class, example = "Password reset code sent to your email")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Email inválido o formato incorrecto",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe una cuenta registrada con ese email",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor al generar el token o enviar el email",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        log.info("Iniciando recuperación de contraseña para email: {}", forgotPasswordRequest.email());
        accountService.forgotPassword(forgotPasswordRequest.email());
        log.info("Email de recuperación enviado a: {}", forgotPasswordRequest.email());
        return new ResponseEntity<>("Password reset code sent to your email", HttpStatus.OK);
    }

    /**
     * Restablece la contraseña del usuario utilizando un token de recuperación.
     *
     * Valida el token de recuperación y actualiza la contraseña del usuario
     * si el token es válido y no ha expirado.
     *
     * @param resetPasswordRequest datos para restablecimiento (token y nueva contraseña)
     * @return ResponseEntity con mensaje de éxito y código HTTP 200 (OK)
     */
    @Operation(
            summary = "Restablecer contraseña",
            description = "Completa el proceso de restablecimiento de contraseña usando el token recibido por email. " +
                    "Valida que el token sea correcto y no haya expirado, luego actualiza la contraseña del usuario " +
                    "con encriptación segura. El token se invalida después de su uso."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Contraseña restablecida exitosamente. Usuario puede iniciar sesión con la nueva contraseña.",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(implementation = String.class, example = "Password reset successfully")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Token inválido, expirado o nueva contraseña no cumple requisitos de seguridad",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró usuario asociado al token proporcionado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor al restablecer la contraseña",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        log.info("Iniciando restablecimiento de contraseña con token: {}", resetPasswordRequest.token());
        accountService.resetPassword(resetPasswordRequest.token(), resetPasswordRequest.newPassword());
        log.info("Contraseña restablecida exitosamente");
        return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
    }
}
