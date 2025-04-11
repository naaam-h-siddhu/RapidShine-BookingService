package com.rapidshine.carwash.bookingservice.dto;

import com.rapidshine.carwash.bookingservice.model.PaymentMethod;
import com.rapidshine.carwash.bookingservice.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    private Long PaymentId;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
}
