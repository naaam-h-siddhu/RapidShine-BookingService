package com.rapidshine.carwash.bookingservice.messaging.rabbitmq;

import com.rapidshine.carwash.bookingservice.dto.WasherStatusUpdateEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WasherStatusPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void updateWashwerStatus(String email, boolean available) {
        WasherStatusUpdateEvent event = new WasherStatusUpdateEvent();
        event.setEmail(email);
        event.setAvailable(available);

        System.out.println("I am here" );
        rabbitTemplate.convertAndSend(
                "washer.status.update.exchange",   // exchange
                "washer.status.update",            // routing key
                event                              // message
        );
    }
}

