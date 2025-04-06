package com.rapidshine.carwash.bookingservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<Car> cars = new ArrayList<>();
    public Customer(String name, String phoneNumber, String address){
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<Booking> bookings = new ArrayList<>();



}
