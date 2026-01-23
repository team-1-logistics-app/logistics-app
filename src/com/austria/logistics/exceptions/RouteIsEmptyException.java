package com.austria.logistics.exceptions;

public class RouteIsEmptyException extends RuntimeException {
    public RouteIsEmptyException(String message) {
        super(message);
    }
}
