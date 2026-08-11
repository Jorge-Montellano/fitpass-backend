package com.fitpass.membership.listener;

import com.fitpass.common.messaging.RabbitMQConfig;
import com.fitpass.membership.entity.UserMembership;
import com.fitpass.membership.repository.UserMembershipRepository;
import com.fitpass.payment.event.PaymentCompletedEvent;
import com.fitpass.payment.event.PaymentFailedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MembershipSagaListener {

    private final UserMembershipRepository userMembershipRepository;

    public MembershipSagaListener(
            UserMembershipRepository userMembershipRepository) {

        this.userMembershipRepository = userMembershipRepository;
    }

    @RabbitListener(
            queues = RabbitMQConfig.MEMBERSHIP_PAYMENT_COMPLETED_QUEUE
    )
    public void handlePaymentCompleted(
            PaymentCompletedEvent event) {

        UserMembership userMembership =
                userMembershipRepository
                        .findById(event.userMembershipId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "UserMembership not found with id: "
                                                + event.userMembershipId()
                                ));

        userMembership.setStatus(1); // ACTIVE

        userMembershipRepository.save(userMembership);

        System.out.println(
                "SAGA -> Membership ACTIVATED"
        );

        System.out.println(
                "UserMembership ID: "
                        + userMembership.getId()
        );
    }

    @RabbitListener(
            queues = RabbitMQConfig.PAYMENT_FAILED_QUEUE
    )
    public void handlePaymentFailed(
            PaymentFailedEvent event) {

        UserMembership userMembership =
                userMembershipRepository
                        .findById(event.userMembershipId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "UserMembership not found with id: "
                                                + event.userMembershipId()
                                ));

        userMembership.setStatus(3); // CANCELLED

        userMembershipRepository.save(userMembership);

        System.out.println(
                "SAGA -> Membership CANCELLED"
        );

        System.out.println(
                "Reason: " + event.reason()
        );
    }
}