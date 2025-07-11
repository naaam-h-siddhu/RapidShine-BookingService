package com.rapidshine.carwash.bookingservice.exceptions;

public class WasherUnavailableException extends RuntimeException{
    public WasherUnavailableException(String message){
        super(message);
    }
}
