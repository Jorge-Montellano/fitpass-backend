package com.fitpass.membership.controller;

import com.fitpass.membership.dto.MembershipPurchaseRequest;
import com.fitpass.membership.entity.UserMembership;
import com.fitpass.membership.service.UserMembershipService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-memberships")
public class UserMembershipController {

    private final UserMembershipService userMembershipService;

    public UserMembershipController(
            UserMembershipService userMembershipService) {
        this.userMembershipService = userMembershipService;
    }

    @PostMapping("/purchase")
    @ResponseStatus(HttpStatus.CREATED)
    public UserMembership purchase(
            @Valid @RequestBody MembershipPurchaseRequest request) {

        return userMembershipService.purchase(request);
    }
}