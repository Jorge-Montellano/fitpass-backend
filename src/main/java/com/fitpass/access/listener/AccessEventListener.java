package com.fitpass.access.listener;

import com.fitpass.access.event.UserCheckedInEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AccessEventListener {

    @EventListener
    public void handleUserCheckedIn(
            UserCheckedInEvent event) {

        System.out.println(
                "EVENT RECEIVED -> UserCheckedIn"
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