package com.rapidshine.carwash.bookingservice.service;

import com.rapidshine.carwash.bookingservice.dto.JobCompletionEvent;
import com.rapidshine.carwash.bookingservice.model.BookingStatus;
import com.rapidshine.carwash.bookingservice.repository.BookingRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingStatusConsumer {
    @Autowired
    private BookingRepository bookingRepository;

    @RabbitListener(queues = "job.completion.update.queue")
    public void handleJobCompletion(JobCompletionEvent event) {
        System.out.println("Received job completion event: " + event);

        bookingRepository.findByBookingByWasherEmail(event.getWasherEmail()).ifPresent(booking -> {
            booking.setBookingStatus(BookingStatus.COMPLETED); // or set as "COMPLETED"
            bookingRepository.save(booking);
        });
    }

}
