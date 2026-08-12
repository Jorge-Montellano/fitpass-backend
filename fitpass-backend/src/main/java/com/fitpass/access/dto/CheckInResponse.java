package com.fitpass.access.dto;

import java.time.LocalDateTime;

public record CheckInResponse(

        Long id,
        Long userId,
        Long gymId,
        LocalDateTime checkInAt,
        LocalDateTime checkOutAt

) {
}