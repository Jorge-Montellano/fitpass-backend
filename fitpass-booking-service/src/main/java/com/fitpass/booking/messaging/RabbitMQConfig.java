package com.fitpass.booking.messaging;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE =
            "fitpass.events";

    public static final String BOOKING_CREATED_ROUTING_KEY =
            "booking.created";

    public static final String BOOKING_CANCELLED_ROUTING_KEY =
            "booking.cancelled";

    @Bean
    public DirectExchange fitpassExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}