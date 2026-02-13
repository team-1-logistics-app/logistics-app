package com.austria.logistics.exceptions;

public class MaxCapacityReachedException extends LogisticsAppException {
    public MaxCapacityReachedException(String message) {
        super(message);
    }
}
