package com.rapidshine.carwash.bookingservice.messaging.rabbitmq;

import com.rapidshine.carwash.bookingservice.dto.WasherStatusUpdateEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WasherServiceCountPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void updateWashwerStatus(String email, boolean available) {
        WasherStatusUpdateEvent event = new WasherStatusUpdateEvent();
        event.setEmail(email);
        event.setAvailable(available);

        rabbitTemplate.convertAndSend(
                "washer.count.update.exchange",   // exchange
                "washer.count.update",            // routing key
                event                              // message
        );
    }
}
