package com.austria.logistics.exceptions;

public class InvalidTruckTypeException extends RuntimeException {
    public InvalidTruckTypeException(String message) {
        super(message);
    }
}
