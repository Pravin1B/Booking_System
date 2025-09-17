package com.booking.exception;

public class ResourceNotFoundException extends BookingException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}