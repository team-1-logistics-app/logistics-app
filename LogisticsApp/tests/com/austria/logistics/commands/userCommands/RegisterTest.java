package com.austria.logistics.commands.userCommands;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.UserImpl;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.models.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class RegisterTest {
    private Repository repository;
    private Command register;
    private User user;

    @BeforeEach
    void setUp(){
        repository = new RepositoryImpl();
        register = new Register(repository);
        user = new UserImpl("Test","Test", "Test","Test","test@test.bg", UserRole.EMPLOYEE);
    }

    @Test
    void executeCommand_Should_Return_Error_When_User_IsLoggedIn() {
        //Arrange
        repository.login(user);
        //Act,Assert
        Assertions.assertEquals("User Test is logged in! Please log out first!",
                register.execute(List.of("Test","Test", "Test","Test","test@test.bg", UserRole.EMPLOYEE.toString())));
    }

    @Test
    void executeCommand_Should_Return_Error_When_ArgumentCount_IsInvalid() {
        //Act,Assert
        Assertions.assertEquals("Invalid number of arguments. Expected: 5, Received: 4.", register.execute(List.of("Test","Test", "Test", "test@test.bg")));
    }

    @Test
    void executeCommand_Should_Return_Error_When_Email_IsInvalid() {
        //Act,Assert
        Assertions.assertEquals("Invalid value for email, it has to be in the format example@example.example!",
                register.execute(List.of("Test","Test", "Test","Test","testtest.bg", UserRole.EMPLOYEE.toString())));
    }

    @Test
    void executeCommand_Should_Return_Error_When_UserRole_IsInvalid() {
        //Act,Assert
        Assertions.assertEquals("Brisbane has to be valid enum value.",
                register.execute(List.of("Test","Test", "Test","Test","test@test.bg", Locations.BRI.getDisplayName())));
    }

    @Test
    void executeCommand_Should_Return_Error_When_Username_Exists() {
        //Arrange
        register.execute(List.of("Test","Test", "Test","Test","test@test.bg", UserRole.EMPLOYEE.toString()));
        //Act,Assert
        Assertions.assertEquals("User Test already exist. Choose a different username!",
                register.execute(List.of("Test","Test", "Test","Test","test@test.bg", UserRole.EMPLOYEE.toString())));
    }

    @Test
    void executeCommand_Should_Return_Error_When_ArgumentLength_Is_Invalid() {
        //Act,Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals("Username must be between 2 and 20 characters long!",
                        register.execute(List.of("T","Test", "Test","Test","test@test.bg", UserRole.EMPLOYEE.toString()))),
                () -> Assertions.assertEquals("Firstname must be between 2 and 20 characters long!",
                        register.execute(List.of("Test","T", "Test","Test","test@test.bg", UserRole.EMPLOYEE.toString()))),
                () -> Assertions.assertEquals("Lastname must be between 2 and 20 characters long!",
                        register.execute(List.of("Test","Test", "T","Test","test@test.bg", UserRole.EMPLOYEE.toString()))),
                () -> Assertions.assertEquals("Password must be between 2 and 20 characters long!",
                        register.execute(List.of("Test","Test", "Test","T","test@test.bg", UserRole.EMPLOYEE.toString())))
        );
    }

    @Test
    void executeCommand_Should_Register_User() {
        //Act
        register.execute(List.of("Test","Test", "Test","Test","test@test.bg", UserRole.EMPLOYEE.toString()));
        // Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, repository.getUsers().size()),
                () -> Assertions.assertEquals("Test",repository.getUsers().get(0).getUsername()),
                () -> Assertions.assertEquals("test@test.bg",repository.getUsers().get(0).getEmail())
        );
    }

}