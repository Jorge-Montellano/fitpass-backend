package com.fitpass.common.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE =
            "fitpass.events";

    public static final String PAYMENT_QUEUE =
            "fitpass.payment.completed";

    public static final String PAYMENT_ROUTING_KEY =
            "payment.completed";

    public static final String CHECKIN_QUEUE =
            "fitpass.user.checked-in";

    public static final String CHECKIN_ROUTING_KEY =
            "user.checked-in";

    @Bean
    public DirectExchange fitpassExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue paymentCompletedQueue() {
        return new Queue(PAYMENT_QUEUE, true);
    }

    @Bean
    public Queue userCheckedInQueue() {
        return new Queue(CHECKIN_QUEUE, true);
    }

    @Bean
    public Binding paymentCompletedBinding(
            Queue paymentCompletedQueue,
            DirectExchange fitpassExchange) {

        return BindingBuilder
                .bind(paymentCompletedQueue)
                .to(fitpassExchange)
                .with(PAYMENT_ROUTING_KEY);
    }

    @Bean
    public Binding userCheckedInBinding(
            Queue userCheckedInQueue,
            DirectExchange fitpassExchange) {

        return BindingBuilder
                .bind(userCheckedInQueue)
                .to(fitpassExchange)
                .with(CHECKIN_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}