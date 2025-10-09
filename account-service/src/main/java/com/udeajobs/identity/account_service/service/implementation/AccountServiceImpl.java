package com.udeajobs.identity.account_service.service.implementation;

import com.udeajobs.identity.account_service.config.RabbitMQConfig;
import com.udeajobs.identity.account_service.entity.User;
import com.udeajobs.identity.account_service.enums.STATUS;
import com.udeajobs.identity.account_service.events.CuentaVerificadaEvent;
import com.udeajobs.identity.account_service.repository.UserRepository;
import com.udeajobs.identity.account_service.service.interfaces.AccountService;
import com.udeajobs.identity.account_service.service.interfaces.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * Implementación del servicio de gestión de cuentas de usuario.
 *
 * Esta clase contiene la lógica de negocio para todas las operaciones relacionadas
 * con el ciclo de vida de las cuentas de usuario, incluyendo registro, verificación,
 * recuperación de contraseñas y comunicación con otros microservicios a través de eventos.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final RabbitTemplate rabbitTemplate;

    /** URL base para enlaces de recuperación de contraseña, configurable por entorno */
    @Value("${app.reset-url-base}")
    private String resetUrlBase;

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * Valida que el email no esté en uso, encripta la contraseña, genera un código
     * de verificación aleatorio y envía un email de confirmación. El usuario queda
     * en estado PENDING_VERIFICATION hasta completar la verificación.
     *
     * @param user el usuario a registrar con datos básicos
     * @return el usuario guardado con información adicional generada
     * @throws IllegalArgumentException si el email ya está registrado
     */
    @Override
    public User registerUser(User user) {
        log.info("Iniciando registro de usuario con email: {}", user.getEmail());

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            log.warn("Intento de registro con email ya existente: {}", user.getEmail());
            throw new IllegalArgumentException("Email already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(STATUS.PENDING_VERIFICATION);
        user.setVerificationCode(String.format("%06d", new Random().nextInt(999999)));
        log.debug("Código de verificación generado para usuario: {}", user.getEmail());

        User savedUser = userRepository.save(user);
        log.info("Usuario guardado en base de datos con ID: {}", savedUser.getId());

        Map<String, Object> model = new HashMap<>();
        model.put("username", savedUser.getUsername());
        model.put("verificationCode", savedUser.getVerificationCode());
        mailService.sendEmail(savedUser.getEmail(), "Verificación de cuenta - UdeAJobs", "verification-email", model);
        log.info("Email de verificación enviado a: {}", savedUser.getEmail());

        return savedUser;
    }

    /**
     * Verifica la cuenta de un usuario utilizando el código de verificación.
     *
     * Valida que el usuario exista y que el código sea correcto, actualiza el estado
     * a ACTIVE, limpia el código de verificación y publica un evento para notificar
     * a otros microservicios sobre la cuenta verificada.
     *
     * @param email el email del usuario a verificar
     * @param verificationCode el código de 6 dígitos recibido por email
     * @throws IllegalArgumentException si el usuario no existe o el código es inválido
     */
    @Override
    public void verifyUser(String email, String verificationCode) {
        log.info("Iniciando verificación de usuario con email: {}", email);

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            log.warn("Intento de verificación para usuario no encontrado: {}", email);
            throw new IllegalArgumentException("User not found");
        }
        if (!user.get().getVerificationCode().equals(verificationCode)) {
            log.warn("Código de verificación inválido para usuario: {}", email);
            throw new IllegalArgumentException("Invalid verification code");
        }

        User savedUser = user.get();
        savedUser.setStatus(STATUS.ACTIVE);
        savedUser.setVerificationCode(null); // Clear verification code after use
        log.debug("Estado de usuario actualizado a ACTIVE para: {}", email);

        CuentaVerificadaEvent cuentaVerificadaEvent = new CuentaVerificadaEvent( savedUser.getId(), savedUser.getFullName(), savedUser.getEmail());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "user.verified", cuentaVerificadaEvent) ;
        log.info("Evento de cuenta verificada publicado para usuario: {}", savedUser.getUsername());

        userRepository.save(savedUser);
        log.info("Usuario {} verificado exitosamente", user.get().getUsername());
    }

    /**
     * Inicia el proceso de recuperación de contraseña.
     *
     * Genera un token UUID único, establece una fecha de expiración de 1 hora,
     * guarda los datos en la base de datos y envía un email con el enlace de recuperación.
     *
     * @param email el email del usuario que solicita recuperar la contraseña
     * @throws IllegalArgumentException si el usuario no existe
     */
    @Override
    public void forgotPassword(String email) {
        log.info("Iniciando proceso de recuperación de contraseña para: {}", email);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            log.warn("Solicitud de recuperación para usuario no encontrado: {}", email);
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiration(LocalDateTime.now().plusHours(1)); // Token expires in 1 hour
        userRepository.save(user);
        log.debug("Token de recuperación generado y guardado para usuario: {}", email);

        String resetUrl = resetUrlBase + "?token=" + token;

        Map<String, Object> model = new HashMap<>();
        model.put("username", user.getUsername());
        model.put("resetUrl", resetUrl);
        mailService.sendEmail(user.getEmail(), "Recuperación de Contraseña - UdeAJobs", "password-reset-link", model);
        log.info("Email de recuperación de contraseña enviado a: {}", email);
    }

    /**
     * Restablece la contraseña del usuario utilizando un token de recuperación.
     *
     * Valida que el token exista y no haya expirado, encripta la nueva contraseña,
     * actualiza los datos del usuario y limpia el token de recuperación.
     *
     * @param token el token UUID de recuperación
     * @param newPassword la nueva contraseña en texto plano (será encriptada)
     * @throws IllegalArgumentException si el token es inválido o ha expirado
     */
    @Override
    public void resetPassword(String token, String newPassword) {
        log.info("Iniciando restablecimiento de contraseña con token: {}", token.substring(0, 8) + "...");

        Optional<User> userOptional = userRepository.findByResetPasswordToken(token);
        if (userOptional.isEmpty()) {
            log.warn("Intento de restablecimiento con token inválido");
            throw new IllegalArgumentException("Invalid token");
        }

        User user = userOptional.get();

        if (user.getResetPasswordTokenExpiration().isBefore(LocalDateTime.now())) {
            log.warn("Intento de restablecimiento con token expirado para usuario: {}", user.getEmail());
            throw new IllegalArgumentException("Token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiration(null);
        userRepository.save(user);
        log.info("Contraseña restablecida exitosamente para usuario: {}", user.getEmail());
    }
}
