package com.udeajobs.identity.account_service.service.implementation;

import com.udeajobs.identity.account_service.service.interfaces.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.util.Map;

/**
 * Implementación del servicio de correo electrónico para entorno de producción.
 *
 * Esta clase maneja el envío real de emails utilizando JavaMailSender y plantillas
 * Thymeleaf. Solo está activa en el perfil de producción para evitar envíos
 * accidentales durante el desarrollo y testing.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    /** Dirección de correo electrónico remitente configurada en las propiedades */
    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Envía un correo electrónico utilizando una plantilla HTML procesada con Thymeleaf.
     *
     * Crea un mensaje MIME, procesa la plantilla con las variables proporcionadas,
     * configura los destinatarios y envía el email. Incluye manejo de errores
     * y logging para monitoreo.
     *
     * @param to dirección de correo electrónico del destinatario
     * @param subject asunto del correo electrónico
     * @param template nombre de la plantilla HTML a procesar
     * @param model mapa con variables para la plantilla
     * @throws RuntimeException si ocurre un error durante el envío
     */
    @Override
    public void sendEmail(String to, String subject, String template, Map<String, Object> model) {
        log.info("Iniciando envío de email a: {} con plantilla: {}", to, template);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariables(model);

            String htmlContent = templateEngine.process(template, context);
            log.debug("Plantilla HTML procesada exitosamente para: {}", to);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Email enviado exitosamente a: {}", to);

        } catch (MessagingException e) {
            log.error("Error al enviar email a: {}", to, e);
            throw new RuntimeException("Error sending email", e);
        }
    }
}
