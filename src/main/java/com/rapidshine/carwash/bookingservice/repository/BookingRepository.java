package com.rapidshine.carwash.bookingservice.repository;

import com.rapidshine.carwash.bookingservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    Booking getBookingsByBookingId(Long bookingId);
    @Query("SELECT b FROM Booking b WHERE b.customer.customerID = :customerId")
    List<Booking> findByCustomerId(@Param("customerId") Long customerId);

}
