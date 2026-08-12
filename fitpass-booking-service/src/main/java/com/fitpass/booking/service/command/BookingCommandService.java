package com.fitpass.booking.service.command;

import com.fitpass.booking.entity.Booking;
import com.fitpass.booking.event.BookingCreatedEvent;
import com.fitpass.booking.messaging.RabbitMQConfig;
import com.fitpass.booking.repository.BookingRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookingCommandService {

    private final BookingRepository bookingRepository;
    private final RabbitTemplate rabbitTemplate;

    public BookingCommandService(
            BookingRepository bookingRepository,
            RabbitTemplate rabbitTemplate) {

        this.bookingRepository = bookingRepository;
        this.rabbitTemplate = rabbitTemplate;
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
                "CQRS COMMAND -> Booking created"
        );

        System.out.println(
                "RABBITMQ EVENT PUBLISHED -> BookingCreated"
        );

        return saved;
    }

    public Booking cancel(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found with id: " + id
                        ));

        booking.setStatus(2); // CANCELLED

        return bookingRepository.save(booking);
    }
}