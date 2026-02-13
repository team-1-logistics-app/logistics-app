package com.austria.logistics.exceptions;

public class LocationNotFoundException extends LogisticsAppException {
    public LocationNotFoundException(String message) {
        super(message);
    }
}
