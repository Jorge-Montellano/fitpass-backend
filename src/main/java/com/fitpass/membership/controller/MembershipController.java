package com.fitpass.membership.controller;

import com.fitpass.membership.dto.MembershipRequest;
import com.fitpass.membership.dto.MembershipResponse;
import com.fitpass.membership.entity.Membership;
import com.fitpass.membership.service.MembershipService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    private final MembershipService membershipService;

    public MembershipController(
            MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @GetMapping
    public List<MembershipResponse> findAll() {

        return membershipService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public MembershipResponse findById(
            @PathVariable Long id) {

        return toResponse(
                membershipService.findById(id)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MembershipResponse create(
            @Valid @RequestBody MembershipRequest request) {

        Membership membership = new Membership();

        membership.setName(request.name());
        membership.setDescription(request.description());
        membership.setPrice(request.price());
        membership.setDurationDays(request.durationDays());
        membership.setStatus(request.status());

        return toResponse(
                membershipService.save(membership)
        );
    }

    @PutMapping("/{id}")
    public MembershipResponse update(
            @PathVariable Long id,
            @Valid @RequestBody MembershipRequest request) {

        Membership membership = new Membership();

        membership.setName(request.name());
        membership.setDescription(request.description());
        membership.setPrice(request.price());
        membership.setDurationDays(request.durationDays());
        membership.setStatus(request.status());

        return toResponse(
                membershipService.update(id, membership)
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {

        membershipService.delete(id);
    }

    private MembershipResponse toResponse(
            Membership membership) {

        return new MembershipResponse(
                membership.getId(),
                membership.getName(),
                membership.getDescription(),
                membership.getPrice(),
                membership.getDurationDays(),
                membership.getStatus()
        );
    }
}