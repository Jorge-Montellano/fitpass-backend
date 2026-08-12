package com.fitpass.booking.service.query;

import com.fitpass.booking.entity.Booking;
import com.fitpass.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingQueryService {

    private final BookingRepository bookingRepository;

    public BookingQueryService(
            BookingRepository bookingRepository) {

        this.bookingRepository = bookingRepository;
    }

    public List<Booking> findAll() {

        System.out.println(
                "CQRS QUERY -> Find all bookings"
        );

        return bookingRepository.findAll();
    }

    public Booking findById(Long id) {

        System.out.println(
                "CQRS QUERY -> Find booking by ID: " + id
        );

        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found with id: " + id
                        ));
    }
}