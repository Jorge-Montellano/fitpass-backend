package com.fitpass.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(

        Long id,
        Long userMembershipId,
        BigDecimal amount,
        LocalDateTime paymentDate,
        String paymentMethod,
        Integer paymentStatus,
        String transactionReference

) {
}