package com.austria.logistics.exceptions;

public class InvalidWeightValueException extends RuntimeException {
    public InvalidWeightValueException(String message) {
        super(message);
    }
}
