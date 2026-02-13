package com.austria.logistics.exceptions;

public class UserAlreadyExistsException extends LogisticsAppException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
