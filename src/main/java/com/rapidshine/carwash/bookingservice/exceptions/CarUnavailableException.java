package com.rapidshine.carwash.bookingservice.exceptions;

import com.rapidshine.carwash.bookingservice.model.Car;

public class CarUnavailableException extends RuntimeException{
    public CarUnavailableException(String message){
        super(message);
    }
}
