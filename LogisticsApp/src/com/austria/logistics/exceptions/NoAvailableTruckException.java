package com.austria.logistics.exceptions;

public class NoAvailableTruckException extends LogisticsAppException {
    public NoAvailableTruckException(String message) {
        super(message);
    }
}
