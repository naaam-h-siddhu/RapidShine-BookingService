package com.rapidshine.carwash.bookingservice.exceptions;

public class PaymentUnavailableException extends RuntimeException   {
    public PaymentUnavailableException(String message){
        super(message);
    }
}
