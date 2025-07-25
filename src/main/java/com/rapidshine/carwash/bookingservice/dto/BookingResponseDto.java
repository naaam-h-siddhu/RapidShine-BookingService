package com.rapidshine.carwash.bookingservice.dto;

import com.rapidshine.carwash.bookingservice.model.BookingStatus;
import com.rapidshine.carwash.bookingservice.model.Payment;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private Long bookingId;
    private LocalDateTime bookingTime;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    private CarDto carDto;
    private PaymentResponseDto payment;
    private  String paymentLink;

    public BookingResponseDto(Long bookingId, LocalDateTime bookingTime, BookingStatus bookingStatus, CarDto carDto, PaymentResponseDto paymentResponseDto) {
        this.bookingId = bookingId;
        this.bookingTime = bookingTime;
        this.bookingStatus = bookingStatus;
        this.carDto = carDto;
        this.payment = paymentResponseDto;

    }
}

