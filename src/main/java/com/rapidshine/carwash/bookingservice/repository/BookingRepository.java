package com.rapidshine.carwash.bookingservice.repository;

import com.rapidshine.carwash.bookingservice.model.Booking;
import com.rapidshine.carwash.bookingservice.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    Booking getBookingsByBookingId(Long bookingId);
    @Query("SELECT b FROM Booking b WHERE b.customer.customerID = :customerId")
    List<Booking> findByCustomerId(@Param("customerId") Long customerId);

    List<Booking> findByBookingStatus(BookingStatus bookingStatus);

    @Query("SELECT b FROM Booking b WHERE b.washerEmail = :email AND b.bookingStatus <> 'COMPLETED'")
    Optional<Booking> findByBookingByWasherEmail(String email);

}
