package com.austria.logistics.exceptions;

public class InvalidLocationRouteException extends RuntimeException {
    public InvalidLocationRouteException(String message) {
        super(message);
    }
}
