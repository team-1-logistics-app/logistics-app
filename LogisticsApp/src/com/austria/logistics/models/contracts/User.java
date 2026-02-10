package com.austria.logistics.models.contracts;

import com.austria.logistics.models.enums.UserRole;

public interface User extends Savealbe{
    String getUsername();
    String getFirstName();
    String getLastName();
    String getPassword();
    UserRole getUserRole();
    boolean isManager();
}
