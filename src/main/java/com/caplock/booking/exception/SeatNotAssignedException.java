package com.caplock.booking.exception;

public class SeatNotAssignedException extends RuntimeException {
    public SeatNotAssignedException(String errorMessage) {
        super(errorMessage);
    }
}
