package com.austria.logistics.exceptions;

public class TruckAlreadyAssignedException extends LogisticsAppException {
    public TruckAlreadyAssignedException(String message) {
        super(message);
    }
}
