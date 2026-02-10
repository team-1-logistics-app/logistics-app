package com.austria.logistics.models.enums;

public enum UserRole {
    CUSTOMER,
    EMPLOYEE ,
    MANAGER;

    @Override
    public String toString() {
        switch (this) {
            case CUSTOMER:
                return "Customer";
            case EMPLOYEE:
                return "Employee";
            case MANAGER:
                return "Manager";
            default:
                return "";
        }
    }
}
