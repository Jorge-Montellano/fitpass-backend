package com.fitpass.membership.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MembershipPurchaseRequestedEvent(
        Long userMembershipId,
        Long userId,
        Long membershipId,
        BigDecimal amount,
        LocalDateTime occurredAt
) {
}