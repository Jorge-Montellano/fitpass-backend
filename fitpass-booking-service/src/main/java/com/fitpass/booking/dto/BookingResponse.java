package com.fitpass.booking.dto;

import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        Long userId,
        Long classId,
        Integer status,
        LocalDateTime createdAt
) {
}