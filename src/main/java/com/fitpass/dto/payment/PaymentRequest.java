package com.fitpass.dto.payment;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentRequest(

        @NotNull
        Long userMembershipId,

        @NotNull
        @DecimalMin("0.0")
        BigDecimal amount,

        @NotNull
        LocalDateTime paymentDate,

        @NotNull
        String paymentMethod,

        Integer paymentStatus,

        String transactionReference

) {
}