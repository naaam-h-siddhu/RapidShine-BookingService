package com.rapidshine.carwash.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StripeRequest {
    private Long amount;
    private Long quantity;
    private String name;
    private String currency;
}
