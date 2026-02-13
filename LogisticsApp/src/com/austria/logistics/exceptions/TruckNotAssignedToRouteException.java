package com.austria.logistics.exceptions;

public class TruckNotAssignedToRouteException extends LogisticsAppException {
    public TruckNotAssignedToRouteException(String message) {
        super(message);
    }
}
