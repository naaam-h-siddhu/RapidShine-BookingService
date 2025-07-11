package com.rapidshine.carwash.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BookingCreatedEvent {
    private String email;
    private Long bookingId;



}
