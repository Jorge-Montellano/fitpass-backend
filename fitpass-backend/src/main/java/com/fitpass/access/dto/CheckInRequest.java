package com.fitpass.access.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CheckInRequest(

        @NotNull
        Long userId,

        @NotNull
        Long gymId,

        LocalDateTime checkInAt,

        LocalDateTime checkOutAt

) {
}