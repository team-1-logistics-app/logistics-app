package com.austria.logistics.exceptions;

public abstract class LogisticsAppException extends RuntimeException {
    public LogisticsAppException(String message) {
        super(message);
    }
}
