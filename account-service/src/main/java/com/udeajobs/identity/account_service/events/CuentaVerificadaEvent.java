package com.udeajobs.identity.account_service.events;

/**
 * Evento que se dispara cuando una cuenta de usuario ha sido verificada exitosamente.
 *
 * Este record representa un evento de dominio que se publica a través de RabbitMQ
 * para notificar a otros microservicios que un usuario ha completado el proceso
 * de verificación de email y su cuenta está ahora activa.
 *
 * @param accountId identificador único de la cuenta verificada
 * @param fullName nombre completo del usuario verificado
 * @param email dirección de correo electrónico del usuario verificado
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
public record CuentaVerificadaEvent(
        String accountId,
        String fullName,
        String email
) {
}
