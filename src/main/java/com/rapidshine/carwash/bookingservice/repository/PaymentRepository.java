package com.rapidshine.carwash.bookingservice.repository;

import com.rapidshine.carwash.bookingservice.model.BookingStatus;
import com.rapidshine.carwash.bookingservice.model.Payment;
import com.rapidshine.carwash.bookingservice.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);
}
