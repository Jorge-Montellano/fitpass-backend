package com.fitpass.notification.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE =
            "fitpass.events";

    public static final String BOOKING_CREATED_QUEUE =
            "fitpass.notification.booking-created";

    public static final String BOOKING_CREATED_ROUTING_KEY =
            "booking.created";

    public static final String BOOKING_CANCELLED_QUEUE =
            "fitpass.notification.booking-cancelled";

    public static final String BOOKING_CANCELLED_ROUTING_KEY =
            "booking.cancelled";

    @Bean
    public DirectExchange fitpassExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue bookingCreatedQueue() {
        return new Queue(BOOKING_CREATED_QUEUE, true);
    }

    @Bean
    public Queue bookingCancelledQueue() {
        return new Queue(BOOKING_CANCELLED_QUEUE, true);
    }

    @Bean
    public Binding bookingCreatedBinding(
            @Qualifier("bookingCreatedQueue")
            Queue bookingCreatedQueue,
            DirectExchange fitpassExchange) {

        return BindingBuilder
                .bind(bookingCreatedQueue)
                .to(fitpassExchange)
                .with(BOOKING_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding bookingCancelledBinding(
            @Qualifier("bookingCancelledQueue")
            Queue bookingCancelledQueue,
            DirectExchange fitpassExchange) {

        return BindingBuilder
                .bind(bookingCancelledQueue)
                .to(fitpassExchange)
                .with(BOOKING_CANCELLED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}