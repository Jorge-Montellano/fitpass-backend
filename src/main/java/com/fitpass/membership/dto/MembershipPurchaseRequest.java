package com.fitpass.membership.dto;

import jakarta.validation.constraints.NotNull;

public record MembershipPurchaseRequest(

        @NotNull
        Long userId,

        @NotNull
        Long membershipId

) {
}