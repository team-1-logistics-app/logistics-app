package com.austria.logistics.models.enums;

public enum UserRole {
    CUSTOMER,
    SUPERVISOR,
    MANAGER;

    @Override
    public String toString() {
        switch (this) {
            case CUSTOMER:
                return "Customer";
            case SUPERVISOR:
                return "Supervisor";
            case MANAGER:
                return "Manager";
            default:
                return "";
        }
    }
}
