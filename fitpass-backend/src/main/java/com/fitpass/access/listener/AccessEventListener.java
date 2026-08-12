package com.fitpass.access.listener;

import com.fitpass.access.event.UserCheckedInEvent;
import com.fitpass.common.messaging.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AccessEventListener {

    @RabbitListener(
            queues = RabbitMQConfig.CHECKIN_QUEUE
    )
    public void handleUserCheckedIn(
            UserCheckedInEvent event) {

        System.out.println(
                "RABBITMQ EVENT RECEIVED -> UserCheckedIn"
        );

        System.out.println(
                "CheckIn ID: " + event.checkInId()
        );

        System.out.println(
                "User ID: " + event.userId()
        );

        System.out.println(
                "Gym ID: " + event.gymId()
        );

        System.out.println(
                "CheckIn Time: " + event.checkInAt()
        );
    }
}