package com.rapidshine.carwash.bookingservice.repository;

import com.rapidshine.carwash.bookingservice.model.Washer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WasherRepository extends JpaRepository<Washer,Long> {
}
