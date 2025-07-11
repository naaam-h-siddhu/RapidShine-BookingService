package com.rapidshine.carwash.bookingservice.messaging.rabbitmq;

import com.rapidshine.carwash.bookingservice.dto.BookingCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingNotificationPublisher {
    public static final String BOOKING_QUEUE_NAME = "booking.notification";
    public static final String BOOKING_EXCHANGE_NAME = "booking.exchange";
    public static final String BOOKING_ROUTING_KEY = "booking.created";
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void bookingNotificationStatus(Long bookingId, String email ){
        BookingCreatedEvent even = new BookingCreatedEvent();
        even.setBookingId(bookingId);
        System.out.println("Notification request receieved to booking");
        even.setEmail(email);
        rabbitTemplate.convertAndSend(
                BOOKING_EXCHANGE_NAME,
                BOOKING_ROUTING_KEY,
                even
        );
    }
}
