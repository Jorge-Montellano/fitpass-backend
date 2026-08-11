package com.fitpass.payment.listener;

import com.fitpass.common.messaging.RabbitMQConfig;
import com.fitpass.payment.event.PaymentCompletedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_QUEUE)
    public void handlePaymentCompleted(PaymentCompletedEvent event) {

        System.out.println(
                "RABBITMQ EVENT RECEIVED -> PaymentCompleted"
        );

        System.out.println("Payment ID: " + event.paymentId());
        System.out.println("Membership ID: " + event.userMembershipId());
        System.out.println("Amount: " + event.amount());
    }
}