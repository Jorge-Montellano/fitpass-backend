package com.fitpass.membership.dto;

import java.math.BigDecimal;

public record MembershipResponse(

        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer durationDays,
        Integer status

) {
}