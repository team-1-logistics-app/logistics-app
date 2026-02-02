package com.austria.logistics.exceptions;

public class NoAvailableTruckException extends RuntimeException {
    public NoAvailableTruckException(String message) {
        super(message);
    }
}
