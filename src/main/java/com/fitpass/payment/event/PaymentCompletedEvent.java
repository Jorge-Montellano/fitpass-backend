package com.fitpass.payment.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentCompletedEvent(
        Long paymentId,
        Long userMembershipId,
        BigDecimal amount,
        String paymentMethod,
        LocalDateTime occurredAt
) {
}