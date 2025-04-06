package com.rapidshine.carwash.bookingservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    private LocalDateTime timestamp;
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private Customer customer;
    private String address;
    private String phoneNumber;

    public User(String name, String email, String phoneNumber, String password, UserRole userRole, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.timestamp = LocalDateTime.now();
        this.address = address;
        this.phoneNumber = phoneNumber;

    }
}