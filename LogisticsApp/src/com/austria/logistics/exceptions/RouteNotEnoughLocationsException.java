package com.austria.logistics.exceptions;

public class RouteNotEnoughLocationsException extends RuntimeException {
    public RouteNotEnoughLocationsException(String message) {
        super(message);
    }
}
