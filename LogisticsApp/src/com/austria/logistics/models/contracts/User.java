package com.austria.logistics.models.contracts;

import com.austria.logistics.models.enums.UserRole;

public interface User extends Savealbe, Printable{
    String getUsername();
    String getFirstName();
    String getLastName();
    String getPassword();
    String getEmail();
    UserRole getUserRole();
    boolean isManager();
    String printMailBox();
    void receiveLetter(String letter);
    String toString();
}
