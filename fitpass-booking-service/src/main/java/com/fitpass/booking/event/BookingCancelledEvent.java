package com.fitpass.booking.event;

import java.time.LocalDateTime;

public record BookingCancelledEvent(
        Long bookingId,
        Long userId,
        Long classId,
        Integer status,
        LocalDateTime occurredAt
) {
}