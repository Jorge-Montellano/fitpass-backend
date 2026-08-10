package com.fitpass.dto.gym;

public record GymResponse(

        Long id,
        String name,
        String description,
        String address,
        String city,
        String phone,
        String email,
        String openingTime,
        String closingTime,
        Integer status

) {
}