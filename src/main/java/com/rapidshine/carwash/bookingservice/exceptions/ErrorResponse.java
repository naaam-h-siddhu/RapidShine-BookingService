package com.rapidshine.carwash.bookingservice.exceptions;

import java.time.LocalDateTime;

public class ErrorResponse {
    public String message;
    public LocalDateTime localDate;
    public int httpStatus;

    public ErrorResponse(String message, LocalDateTime localDate, int httpStatus) {
        this.message = message;
        this.localDate = localDate;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public ErrorResponse(String message) {
        this.message = message;
    }
}
