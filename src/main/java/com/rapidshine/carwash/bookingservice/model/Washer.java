package com.rapidshine.carwash.bookingservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "washer")
@Data
@NoArgsConstructor
public class Washer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long washerId;
    private String name;
    private String email;
    @OneToOne
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    @JsonBackReference
    private User user;
    private String phoneNumber;
    private String address;
    private boolean isAvailable;

    public Washer(String name, String email, String address, String phoneNumber, boolean isAvailable){
        this.name= name;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.isAvailable = isAvailable;
    }

}
