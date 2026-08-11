package com.fitpass.payment.event;

import java.time.LocalDateTime;

public record PaymentFailedEvent(
        Long userMembershipId,
        String reason,
        LocalDateTime occurredAt
) {
}