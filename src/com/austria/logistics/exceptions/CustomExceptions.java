package com.austria.logistics.exceptions;

public class InvalidLocation extends RuntimeException {
    public InvalidLocation(String message) {
        super(message);
    }
}
