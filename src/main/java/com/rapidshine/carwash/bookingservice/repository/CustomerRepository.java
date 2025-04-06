package com.rapidshine.carwash.bookingservice.repository;

import com.rapidshine.carwash.bookingservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    @Query("SELECT c FROM Customer c WHERE c.user.id = :userId")
    Optional<Customer> findByUserId(@Param("userId") Long userId);

}
