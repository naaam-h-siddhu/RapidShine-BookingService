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
@Table(name = "cars")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;
    private String brand;

    public Car(String brand, String model, String licenceNumberPlate) {
        this.brand = brand;
        this.model = model;
        this.licenceNumberPlate = licenceNumberPlate;
    }


    private String model;

    @Column(unique = true,nullable = false)
    private String licenceNumberPlate;
    @ManyToOne
    @JoinColumn(name = "customer_id",nullable = false)
    @JsonBackReference
    private Customer customer;
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<Booking> bookings = new ArrayList<>();

    public Car(Long carId, String brand, String model, String licenceNumberPlate) {
        this.carId = carId;
        this.brand = brand;
        this.model  = model;
        this.licenceNumberPlate = licenceNumberPlate;
    }
}
