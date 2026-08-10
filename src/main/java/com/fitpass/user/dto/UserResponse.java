package com.fitpass.user.dto;

public record UserResponse(

        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        Integer status,
        Long roleId,
        String roleName,
        Long gymId,
        String gymName

) {
}