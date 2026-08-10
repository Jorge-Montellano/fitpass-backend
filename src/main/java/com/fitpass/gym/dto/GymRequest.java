package com.fitpass.gym.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record GymRequest(

        @NotBlank
        String name,

        String description,

        @NotBlank
        String address,

        @NotBlank
        String city,

        String phone,

        @Email
        String email,

        String openingTime,

        String closingTime,

        Integer status

) {
}