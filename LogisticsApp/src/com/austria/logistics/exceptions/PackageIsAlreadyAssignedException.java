package com.austria.logistics.exceptions;

public class PackageIsAlreadyAssignedException extends RuntimeException {
    public PackageIsAlreadyAssignedException(String message) {
        super(message);
    }
}
