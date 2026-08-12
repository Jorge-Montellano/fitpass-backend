package com.fitpass.booking.controller;

import com.fitpass.booking.dto.BookingRequest;
import com.fitpass.booking.dto.BookingResponse;
import com.fitpass.booking.entity.Booking;
import com.fitpass.booking.service.command.BookingCommandService;
import com.fitpass.booking.service.query.BookingQueryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingCommandService bookingCommandService;
    private final BookingQueryService bookingQueryService;

    public BookingController(
            BookingCommandService bookingCommandService,
            BookingQueryService bookingQueryService) {

        this.bookingCommandService = bookingCommandService;
        this.bookingQueryService = bookingQueryService;
    }
    @GetMapping
    public List<BookingResponse> findAll() {

        return bookingQueryService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public BookingResponse findById(@PathVariable Long id) {
        return toResponse(
                bookingQueryService.findById(id)
        );
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse create(
            @Valid @RequestBody BookingRequest request) {

        Booking booking = new Booking();

        booking.setUserId(request.userId());
        booking.setClassId(request.classId());

        return toResponse(
                bookingCommandService.create(booking)
        );
    }

    @PutMapping("/{id}/cancel")
    public BookingResponse cancel(
            @PathVariable Long id) {

        return toResponse(
                bookingCommandService.cancel(id)
        );
    }

    private BookingResponse toResponse(
            Booking booking) {

        return new BookingResponse(
                booking.getId(),
                booking.getUserId(),
                booking.getClassId(),
                booking.getStatus(),
                booking.getCreatedAt()
        );
    }
}