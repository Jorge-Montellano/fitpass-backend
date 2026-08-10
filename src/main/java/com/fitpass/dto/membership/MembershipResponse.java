package com.fitpass.dto.membership;

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