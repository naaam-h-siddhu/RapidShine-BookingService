package com.rapidshine.carwash.bookingservice.exceptions;

public class InvalidJwtTokenException extends RuntimeException{
    public InvalidJwtTokenException(String message) {
        super(message);

    }
}
