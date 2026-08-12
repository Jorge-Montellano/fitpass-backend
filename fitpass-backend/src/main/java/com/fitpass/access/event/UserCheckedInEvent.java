package com.fitpass.access.event;

import java.time.LocalDateTime;

public record UserCheckedInEvent(
        Long checkInId,
        Long userId,
        Long gymId,
        LocalDateTime checkInAt,
        LocalDateTime occurredAt
) {
}