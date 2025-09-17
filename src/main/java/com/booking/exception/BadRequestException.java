package com.booking.exception;

public class BadRequestException extends BookingException {
    public BadRequestException(String message) {
        super(message);
    }
}