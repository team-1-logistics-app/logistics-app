package com.austria.logistics.models;

import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.utils.Parsers;
import com.austria.logistics.utils.Validators;

import static java.lang.String.format;

public class UserImpl implements User {

    private static final int LEN_MIN = 2;
    private static final int LEN_MAX = 20;
    private static final String USERNAME_LEN_ERR = format("Username must be between %d and %d characters long!", LEN_MIN, LEN_MAX);
    private static final String FIRSTNAME_LEN_ERR = format("Firstname must be between %s and %s characters long!", LEN_MIN, LEN_MAX);
    private static final String LASTNAME_LEN_ERR = format("Lastname must be between %s and %s characters long!", LEN_MIN, LEN_MAX);
    private static final String PASSWORD_LEN_ERR = format("Password must be between %s and %s characters long!", LEN_MIN, LEN_MAX);

    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private UserRole userRole;

    public UserImpl(String username, String firstName, String lastName, String password, UserRole userRole) {
        this.setUsername(username);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setPassword(password);
        this.setUserRole(userRole);
    }

    private void setUsername(String username) {
        Validators.validateStringLenght(username, LEN_MIN, LEN_MAX, USERNAME_LEN_ERR);
        this.username = username;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    private void setFirstName(String firstName) {
        Validators.validateStringLenght(firstName, LEN_MIN, LEN_MAX, FIRSTNAME_LEN_ERR);
        this.firstName = firstName;
    }

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    private void setLastName(String lastName) {
        Validators.validateStringLenght(lastName, LEN_MIN, LEN_MAX, LASTNAME_LEN_ERR);
        this.lastName = lastName;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }

    private void setPassword(String password) {
        Validators.validateStringLenght(password, LEN_MIN, LEN_MAX, PASSWORD_LEN_ERR);
        this.password = password;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    private void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public UserRole getUserRole() {
        return this.userRole;
    }

    @Override
    public boolean isManager() {
        return this.userRole == UserRole.MANAGER;
    }

    @Override
    public String toSaveString() {
        return String.join("|",
                username,
                firstName,
                lastName,
                password,
                userRole.toString());
    }
}
