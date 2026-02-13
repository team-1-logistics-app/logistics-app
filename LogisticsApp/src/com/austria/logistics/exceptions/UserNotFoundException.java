package com.austria.logistics.exceptions;

public class UserNotFoundException extends LogisticsAppException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
