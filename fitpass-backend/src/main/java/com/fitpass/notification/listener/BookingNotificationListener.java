package com.fitpass.notification.listener;

import com.fitpass.booking.event.BookingCreatedEvent;
import com.fitpass.common.messaging.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BookingNotificationListener {

    @RabbitListener(
            queues = RabbitMQConfig.BOOKING_CREATED_QUEUE
    )
    public void handleBookingCreated(
            BookingCreatedEvent event) {

        System.out.println(
                "NOTIFICATION EVENT RECEIVED -> BookingCreated"
        );

        System.out.println(
                "Booking ID: " + event.bookingId()
        );

        System.out.println(
                "User ID: " + event.userId()
        );

        System.out.println(
                "Class ID: " + event.classId()
        );

        System.out.println(
                "NOTIFICATION SENT -> Booking confirmed"
        );
    }
}