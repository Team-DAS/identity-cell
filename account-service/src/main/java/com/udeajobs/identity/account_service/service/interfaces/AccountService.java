package com.udeajobs.identity.account_service.service.interfaces;

import com.udeajobs.identity.account_service.entity.User;

/**
 * Interfaz de servicio para operaciones de gestión de cuentas de usuario.
 *
 * Define el contrato para todas las operaciones relacionadas con el ciclo de vida
 * de las cuentas de usuario, incluyendo registro, verificación y recuperación
 * de contraseñas. Esta interfaz es implementada por AccountServiceImpl.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
public interface AccountService {

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * Encripta la contraseña, genera un código de verificación,
     * guarda el usuario en la base de datos y envía un email de verificación.
     *
     * @param user el usuario a registrar
     * @return el usuario registrado con información adicional (ID, código de verificación, etc.)
     * @throws IllegalArgumentException si el email ya está en uso
     */
    User registerUser(User user);

    /**
     * Verifica la cuenta de un usuario utilizando el código de verificación.
     *
     * Valida el código de verificación, activa la cuenta del usuario
     * y publica un evento de cuenta verificada.
     *
     * @param email el email del usuario a verificar
     * @param verificationCode el código de verificación recibido por email
     * @throws IllegalArgumentException si el usuario no existe o el código es inválido
     */
    void verifyUser(String email, String verificationCode);

    /**
     * Inicia el proceso de recuperación de contraseña.
     *
     * Genera un token de recuperación temporal, lo guarda en la base de datos
     * y envía un email con el enlace de recuperación.
     *
     * @param email el email del usuario que solicita recuperar la contraseña
     * @throws IllegalArgumentException si el usuario no existe
     */
    void forgotPassword(String email);

    /**
     * Restablece la contraseña de un usuario utilizando un token de recuperación.
     *
     * Valida el token, verifica que no haya expirado, actualiza la contraseña
     * y limpia el token de recuperación.
     *
     * @param token el token de recuperación de contraseña
     * @param newPassword la nueva contraseña del usuario
     * @throws IllegalArgumentException si el token es inválido o ha expirado
     */
    void resetPassword(String token, String newPassword);
}
