package com.fitpass.notification.listener;

import com.fitpass.notification.entity.Notification;
import com.fitpass.notification.event.BookingCancelledEvent;
import com.fitpass.notification.event.BookingCreatedEvent;
import com.fitpass.notification.messaging.RabbitMQConfig;
import com.fitpass.notification.repository.NotificationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BookingNotificationListener {

    private final NotificationRepository notificationRepository;

    public BookingNotificationListener(
            NotificationRepository notificationRepository) {

        this.notificationRepository = notificationRepository;
    }

    @RabbitListener(
            queues = RabbitMQConfig.BOOKING_CREATED_QUEUE
    )
    public void handleBookingCreated(
            BookingCreatedEvent event) {

        System.out.println(
                "NOTIFICATION EVENT RECEIVED -> BookingCreated"
        );

        Notification notification =
                Notification.builder()
                        .userId(event.userId())
                        .bookingId(event.bookingId())
                        .type("BOOKING_CREATED")
                        .message("Your FitPass booking was confirmed")
                        .status(1)
                        .sentAt(LocalDateTime.now())
                        .build();

        notificationRepository.save(notification);

        System.out.println(
                "NOTIFICATION SAVED -> Booking confirmed"
        );
    }

    @RabbitListener(
            queues = RabbitMQConfig.BOOKING_CANCELLED_QUEUE
    )
    public void handleBookingCancelled(
            BookingCancelledEvent event) {

        System.out.println(
                "NOTIFICATION EVENT RECEIVED -> BookingCancelled"
        );

        Notification notification =
                Notification.builder()
                        .userId(event.userId())
                        .bookingId(event.bookingId())
                        .type("BOOKING_CANCELLED")
                        .message("Your FitPass booking was cancelled")
                        .status(1)
                        .sentAt(LocalDateTime.now())
                        .build();

        notificationRepository.save(notification);

        System.out.println(
                "NOTIFICATION SAVED -> Booking cancelled"
        );
    }
}