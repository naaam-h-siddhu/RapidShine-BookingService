package com.rapidshine.carwash.bookingservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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


    private String carType;
    private String model;
    private String licenceNumberPlate;
    @ManyToOne
    @JoinColumn(name = "customer_id",nullable = false)
    @JsonBackReference
    private Customer customer;

    public Car(Long carId, String brand, String carType, String licenceNumberPlate) {
        this.carId = carId;
        this.brand = brand;
        this.carType = carType;
        this.licenceNumberPlate = licenceNumberPlate;

    }
}
