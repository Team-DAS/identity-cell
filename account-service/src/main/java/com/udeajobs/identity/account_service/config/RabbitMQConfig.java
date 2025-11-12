package com.udeajobs.identity.account_service.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de RabbitMQ para el servicio de cuentas.
 *
 * Esta clase configura los componentes necesarios de RabbitMQ para permitir
 * la comunicación asíncrona entre microservicios a través de eventos.
 * Utiliza un patrón de Topic Exchange para routing flexible de mensajes.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class RabbitMQConfig {

    /** Nombre del exchange principal para eventos del servicio de cuentas */
    public static final String EXCHANGE_NAME = "account.exchange";

    /**
     * Configura el Topic Exchange para el servicio de cuentas.
     *
     * El Topic Exchange permite routing de mensajes basado en patrones de routing keys,
     * proporcionando flexibilidad para diferentes tipos de eventos del servicio.
     *
     * @return una instancia configurada de TopicExchange
     */
    @Bean
    public TopicExchange accountServiceExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
