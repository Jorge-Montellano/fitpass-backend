package com.fitpass.membership.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MembershipRequest(

        @NotBlank
        String name,

        String description,

        @NotNull
        @DecimalMin("0.0")
        BigDecimal price,

        @NotNull
        Integer durationDays,

        Integer status

) {
}