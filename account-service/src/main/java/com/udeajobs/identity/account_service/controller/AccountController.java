package com.udeajobs.identity.account_service.controller;


import com.udeajobs.identity.account_service.dto.RegistrationRequest;
import com.udeajobs.identity.account_service.dto.ResetPasswordRequest;
import com.udeajobs.identity.account_service.dto.VerificationRequest;
import com.udeajobs.identity.account_service.dto.ForgotPasswordRequest;
import com.udeajobs.identity.account_service.entity.User;
import com.udeajobs.identity.account_service.service.interfaces.AccountService;
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
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        log.info("Iniciando restablecimiento de contraseña con token: {}", resetPasswordRequest.token());
        accountService.resetPassword(resetPasswordRequest.token(), resetPasswordRequest.newPassword());
        log.info("Contraseña restablecida exitosamente");
        return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
    }
}
