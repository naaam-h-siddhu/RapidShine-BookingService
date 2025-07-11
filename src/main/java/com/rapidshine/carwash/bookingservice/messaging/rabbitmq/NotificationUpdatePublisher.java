package com.rapidshine.carwash.bookingservice.messaging.rabbitmq;

import com.rapidshine.carwash.bookingservice.dto.BookingCreatedEvent;
import com.rapidshine.carwash.bookingservice.dto.NotificationRequestDto;
import com.rapidshine.carwash.bookingservice.model.UserRole;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationUpdatePublisher {
    // Notification Db update request
    public static final String NOTIFICATION_UPDATE_QUEUE = "notification.request.queue";
    public static  final  String NOTIFICATION_UPDATE_EXCHANGE = "notification.request.exchange";
    public static final String NOTIFICATION_UPDATE_ROUTING_KEY = "notification.request.routing.key";
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void addNotification(String email, String message, UserRole userRole){
        NotificationRequestDto notificationRequestDto = new NotificationRequestDto();
        notificationRequestDto.setEmail(email);
        notificationRequestDto.setMessage(message);
        notificationRequestDto.setRole(userRole);
        rabbitTemplate.convertAndSend(
                NOTIFICATION_UPDATE_EXCHANGE,
                NOTIFICATION_UPDATE_ROUTING_KEY,
                notificationRequestDto
        );
    }
}
