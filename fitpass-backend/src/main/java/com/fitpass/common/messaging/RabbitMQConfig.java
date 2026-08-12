package com.fitpass.common.messaging;

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

    public static final String PAYMENT_QUEUE =
            "fitpass.payment.completed";

    public static final String PAYMENT_ROUTING_KEY =
            "payment.completed";

    public static final String CHECKIN_QUEUE =
            "fitpass.user.checked-in";

    public static final String CHECKIN_ROUTING_KEY =
            "user.checked-in";

    public static final String MEMBERSHIP_PURCHASE_QUEUE =
            "fitpass.membership.purchase.requested";

    public static final String MEMBERSHIP_PURCHASE_ROUTING_KEY =
            "membership.purchase.requested";

    public static final String PAYMENT_FAILED_QUEUE =
            "fitpass.payment.failed";

    public static final String PAYMENT_FAILED_ROUTING_KEY =
            "payment.failed";

    public static final String MEMBERSHIP_PAYMENT_COMPLETED_QUEUE =
            "fitpass.membership.payment.completed";


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
    public Queue membershipPurchaseQueue() {
        return new Queue(MEMBERSHIP_PURCHASE_QUEUE, true);
    }


    @Bean
    public Queue paymentFailedQueue() {
        return new Queue(PAYMENT_FAILED_QUEUE, true);
    }


    @Bean
    public Queue membershipPaymentCompletedQueue() {
        return new Queue(
                MEMBERSHIP_PAYMENT_COMPLETED_QUEUE,
                true
        );
    }


    @Bean
    public Binding paymentCompletedBinding(
            @Qualifier("paymentCompletedQueue")
            Queue paymentCompletedQueue,
            DirectExchange fitpassExchange) {

        return BindingBuilder
                .bind(paymentCompletedQueue)
                .to(fitpassExchange)
                .with(PAYMENT_ROUTING_KEY);
    }


    @Bean
    public Binding userCheckedInBinding(
            @Qualifier("userCheckedInQueue")
            Queue userCheckedInQueue,
            DirectExchange fitpassExchange) {

        return BindingBuilder
                .bind(userCheckedInQueue)
                .to(fitpassExchange)
                .with(CHECKIN_ROUTING_KEY);
    }


    @Bean
    public Binding membershipPurchaseBinding(
            @Qualifier("membershipPurchaseQueue")
            Queue membershipPurchaseQueue,
            DirectExchange fitpassExchange) {

        return BindingBuilder
                .bind(membershipPurchaseQueue)
                .to(fitpassExchange)
                .with(MEMBERSHIP_PURCHASE_ROUTING_KEY);
    }


    @Bean
    public Binding paymentFailedBinding(
            @Qualifier("paymentFailedQueue")
            Queue paymentFailedQueue,
            DirectExchange fitpassExchange) {

        return BindingBuilder
                .bind(paymentFailedQueue)
                .to(fitpassExchange)
                .with(PAYMENT_FAILED_ROUTING_KEY);
    }


    @Bean
    public Binding membershipPaymentCompletedBinding(
            @Qualifier("membershipPaymentCompletedQueue")
            Queue queue,
            DirectExchange fitpassExchange) {

        return BindingBuilder
                .bind(queue)
                .to(fitpassExchange)
                .with(PAYMENT_ROUTING_KEY);
    }


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}