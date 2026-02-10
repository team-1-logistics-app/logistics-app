package com.austria.logistics.exceptions;

public class FailedToLoadFromFileException extends RuntimeException {
    public FailedToLoadFromFileException(String message) {
        super(message);
    }
}
