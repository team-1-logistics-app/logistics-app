package com.austria.logistics.exceptions;

public class PackageIsAlreadyAssignedException extends LogisticsAppException {
    public PackageIsAlreadyAssignedException(String message) {
        super(message);
    }
}
