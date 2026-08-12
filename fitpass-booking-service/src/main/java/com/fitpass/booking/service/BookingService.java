package com.fitpass.booking.service;

import com.fitpass.booking.entity.Booking;
import com.fitpass.booking.event.BookingCancelledEvent;
import com.fitpass.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;
import com.fitpass.booking.event.BookingCreatedEvent;
import com.fitpass.booking.messaging.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RabbitTemplate rabbitTemplate;

    public BookingService(BookingRepository bookingRepository,
                          RabbitTemplate rabbitTemplate) {
        this.bookingRepository = bookingRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found with id: " + id
                        ));
    }

    public Booking create(Booking booking) {
        booking.setStatus(1); // CONFIRMED
        Booking saved = bookingRepository.save(booking);

        BookingCreatedEvent event =
                new BookingCreatedEvent(
                        saved.getId(),
                        saved.getUserId(),
                        saved.getClassId(),
                        saved.getStatus(),
                        LocalDateTime.now()
                );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.BOOKING_CREATED_ROUTING_KEY,
                event
        );

        System.out.println(
                "RABBITMQ EVENT PUBLISHED -> BookingCreated"
        );

        System.out.println(
                "Booking ID: " + saved.getId()
        );

        return saved;
    }

    public Booking cancel(Long id) {

        Booking booking = findById(id);

        booking.setStatus(2); // CANCELLED

        Booking saved = bookingRepository.save(booking);

        BookingCancelledEvent event =
                new BookingCancelledEvent(
                        saved.getId(),
                        saved.getUserId(),
                        saved.getClassId(),
                        saved.getStatus(),
                        LocalDateTime.now()
                );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.BOOKING_CANCELLED_ROUTING_KEY,
                event
        );


        System.out.println(
                "RABBITMQ EVENT PUBLISHED -> BookingCancelled"
        );

        System.out.println(
                "Booking ID: " + saved.getId()
        );

        return saved;
    }
}