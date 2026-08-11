package com.fitpass.payment.listener;

import com.fitpass.payment.event.PaymentCompletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    @EventListener
    public void handlePaymentCompleted(
            PaymentCompletedEvent event) {

        System.out.println(
                "EVENT RECEIVED -> PaymentCompleted"
        );

        System.out.println(
                "Payment ID: " + event.paymentId()
        );

        System.out.println(
                "Membership ID: " + event.userMembershipId()
        );

        System.out.println(
                "Amount: " + event.amount()
        );
    }
}