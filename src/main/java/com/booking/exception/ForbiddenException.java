package com.booking.exception;

// For User not allowed
public class ForbiddenException extends BookingException {
    public ForbiddenException(String message) {
        super(message);
    }
}