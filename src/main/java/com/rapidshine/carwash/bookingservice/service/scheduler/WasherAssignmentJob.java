package com.rapidshine.carwash.bookingservice.service.scheduler;

import com.rapidshine.carwash.bookingservice.messaging.rabbitmq.NotificationUpdatePublisher;
import com.rapidshine.carwash.bookingservice.messaging.rabbitmq.WasherStatusPublisher;
import com.rapidshine.carwash.bookingservice.model.*;
import com.rapidshine.carwash.bookingservice.repository.BookingRepository;
import com.rapidshine.carwash.bookingservice.service.BookingIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class WasherAssignmentJob
{
    @Autowired
    private BookingIntegrationService bookingIntegrationService;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private WasherStatusPublisher washerStatusPublisher;
    @Autowired
    private NotificationUpdatePublisher notificationUpdatePublisher;
    @Scheduled(fixedRate = 30000)
    public String assignWasher() {
        StringBuilder stringBuilder = new StringBuilder();
        List<Booking> unassignedBookings = bookingRepository.findByBookingStatus(BookingStatus.PAYMENT_DONE);

        if (unassignedBookings.isEmpty()) {
            return stringBuilder.append("No booking left for assignment").toString();
        }

        List<Washer> washers = bookingIntegrationService.getListOfWasher();
        if (washers.isEmpty() || washers.get(0).getName().equals("Unavailable")) {
            return stringBuilder.append("No washer left for assignment").toString();
        }

        Random random = new Random();
        for (Booking booking1 : unassignedBookings) {
            if (booking1.getPayment().getPaymentStatus() == PaymentStatus.PAID) {
                Washer washer = washers.get(random.nextInt(washers.size()));
                booking1.setWasherEmail(washer.getEmail());
                booking1.setBookingStatus(BookingStatus.CONFIRMED);
                bookingRepository.save(booking1);
                washerStatusPublisher.updateWashwerStatus(washer.getEmail(), false);
                notificationUpdatePublisher.addNotification(booking1.getCustomer().getEmail(),"Washer "+washer.getName()+" is assigned for your booking#"+booking1.getBookingId(), UserRole.CUSTOMER);
                notificationUpdatePublisher.addNotification(washer.getEmail(),booking1.getBookingId()+" is assigned for washing ",UserRole.WASHER);
                stringBuilder.append(booking1.getBookingId()).append(washer.getEmail()).append("\n");
            }
        }

        return stringBuilder.toString();
    }
}
