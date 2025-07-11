package com.rapidshine.carwash.bookingservice.messaging.rabbitmq;

import com.rapidshine.carwash.bookingservice.dto.BookingPaymentStatusEvent;

import com.rapidshine.carwash.bookingservice.model.BookingStatus;
import com.rapidshine.carwash.bookingservice.repository.BookingRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingStatusPaymentConsumer {
    @Autowired
    private BookingRepository bookingRepository;

    @RabbitListener(queues = "booking.payment.notification")
    public void handleJobCompletion(BookingPaymentStatusEvent event) {
        System.out.println("Received job Payment Status event: " + event);

        bookingRepository.findById(event.getBooking_id()).ifPresent(booking -> {
            booking.setBookingStatus(BookingStatus.PAYMENT_DONE);
            bookingRepository.save(booking);
        });
    }

}
