package com.rapidshine.carwash.bookingservice.service;

import com.rapidshine.carwash.bookingservice.dto.WasherStatusUpdateEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class WasherStatusPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void updateWashwerStatus(String email, boolean available) {
        WasherStatusUpdateEvent event = new WasherStatusUpdateEvent();
        event.setEmail(email);
        event.setAvailable(available);

        rabbitTemplate.convertAndSend(
                "washer.status.update.exchange",   // exchange
                "washer.status.update",            // routing key
                event                              // message
        );
    }
}

