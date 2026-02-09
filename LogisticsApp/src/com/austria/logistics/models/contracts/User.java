package com.austria.logistics.models.contracts;

import com.austria.logistics.models.enums.UserRole;

public interface User {
    String getUsername();
    String getFirstName();
    String getLastName();
    String getPassword();
    UserRole getUserRole();
    boolean isManager();
}
