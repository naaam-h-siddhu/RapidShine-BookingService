package com.rapidshine.carwash.bookingservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
