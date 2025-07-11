package com.rapidshine.carwash.bookingservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerID;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonBackReference
    private User user;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private int total_bookings;
    @Enumerated(EnumType.STRING)
    private Customer_status customerStatus;
    private LocalDateTime timeStamp;
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<Car> cars = new ArrayList<>();
    public Customer(String name,String email,String phoneNumber,String address){
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.timeStamp = LocalDateTime.now();
        this.total_bookings = 0;
        this.customerStatus = Customer_status.ACTIVE;

    }



}
