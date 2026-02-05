package com.austria.logistics.exceptions;

public class TruckNotAssignedToRouteException extends RuntimeException {
    public TruckNotAssignedToRouteException(String message) {
        super(message);
    }
}
