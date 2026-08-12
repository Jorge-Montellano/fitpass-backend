package com.fitpass.booking.dto;

import jakarta.validation.constraints.NotNull;

public record BookingRequest(

        @NotNull
        Long userId,

        @NotNull
        Long classId

) {
}