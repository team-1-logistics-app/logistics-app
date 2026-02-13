package com.austria.logistics.exceptions;

public class RouteNotEnoughLocationsException extends LogisticsAppException {
    public RouteNotEnoughLocationsException(String message) {
        super(message);
    }
}
