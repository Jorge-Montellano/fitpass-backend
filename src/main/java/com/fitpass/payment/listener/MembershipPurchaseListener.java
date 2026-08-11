package com.fitpass.payment.listener;

import com.fitpass.common.messaging.RabbitMQConfig;
import com.fitpass.membership.event.MembershipPurchaseRequestedEvent;
import com.fitpass.payment.event.PaymentCompletedEvent;
import com.fitpass.payment.event.PaymentFailedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MembershipPurchaseListener {

    private final RabbitTemplate rabbitTemplate;

    public MembershipPurchaseListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(
            queues = RabbitMQConfig.MEMBERSHIP_PURCHASE_QUEUE
    )
    public void handleMembershipPurchase(
            MembershipPurchaseRequestedEvent event) {

        System.out.println(
                "RABBITMQ EVENT RECEIVED -> MembershipPurchaseRequested"
        );

        System.out.println(
                "UserMembership ID: " + event.userMembershipId()
        );

        System.out.println(
                "Amount: " + event.amount()
        );

        if (event.amount() != null
                && event.amount().signum() > 0) {

            PaymentCompletedEvent completedEvent =
                    new PaymentCompletedEvent(
                            null,
                            event.userMembershipId(),
                            event.amount(),
                            "SIMULATED",
                            LocalDateTime.now()
                    );

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.PAYMENT_ROUTING_KEY,
                    completedEvent
            );

        } else {

            PaymentFailedEvent failedEvent =
                    new PaymentFailedEvent(
                            event.userMembershipId(),
                            "Invalid payment amount",
                            LocalDateTime.now()
                    );

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.PAYMENT_FAILED_ROUTING_KEY,
                    failedEvent
            );
        }
    }
}