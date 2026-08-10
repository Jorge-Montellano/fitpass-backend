package com.fitpass.controller;

import com.fitpass.dto.checkin.CheckInRequest;
import com.fitpass.dto.checkin.CheckInResponse;
import com.fitpass.entity.CheckIn;
import com.fitpass.service.CheckInService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/check-ins")
public class CheckInController {

    private final CheckInService checkInService;

    public CheckInController(
            CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @GetMapping
    public List<CheckInResponse> findAll() {

        return checkInService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public CheckInResponse findById(
            @PathVariable Long id) {

        return toResponse(
                checkInService.findById(id)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CheckInResponse create(
            @Valid @RequestBody CheckInRequest request) {

        CheckIn checkIn = new CheckIn();

        checkIn.setCheckInAt(request.checkInAt());
        checkIn.setCheckOutAt(request.checkOutAt());

        return toResponse(
                checkInService.save(checkIn)
        );
    }

    @PutMapping("/{id}")
    public CheckInResponse update(
            @PathVariable Long id,
            @Valid @RequestBody CheckInRequest request) {

        CheckIn checkIn = new CheckIn();

        checkIn.setCheckInAt(request.checkInAt());
        checkIn.setCheckOutAt(request.checkOutAt());

        return toResponse(
                checkInService.update(id, checkIn)
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {

        checkInService.delete(id);
    }

    private CheckInResponse toResponse(CheckIn checkIn) {

        return new CheckInResponse(
                checkIn.getId(),
                checkIn.getUser() != null
                        ? checkIn.getUser().getId()
                        : null,
                checkIn.getGym() != null
                        ? checkIn.getGym().getId()
                        : null,
                checkIn.getCheckInAt(),
                checkIn.getCheckOutAt()
        );
    }
}