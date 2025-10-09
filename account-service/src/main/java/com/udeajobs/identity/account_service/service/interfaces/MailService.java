package com.udeajobs.identity.account_service.service.interfaces;

import java.util.Map;

/**
 * Interfaz de servicio para operaciones de envío de correos electrónicos.
 *
 * Define el contrato para el envío de emails utilizando plantillas HTML
 * personalizables. Permite enviar diferentes tipos de correos como
 * verificación de cuenta y recuperación de contraseña.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
public interface MailService {

    /**
     * Envía un correo electrónico utilizando una plantilla HTML.
     *
     * Procesa la plantilla especificada con el modelo de datos proporcionado
     * y envía el email resultante al destinatario.
     *
     * @param to dirección de correo electrónico del destinatario
     * @param subject asunto del correo electrónico
     * @param template nombre de la plantilla HTML a utilizar (sin extensión)
     * @param model mapa con variables para personalizar la plantilla
     */
    void sendEmail(String to, String subject, String template, Map<String, Object> model);
}
