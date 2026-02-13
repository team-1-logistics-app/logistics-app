package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.commands.userCommands.Register;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.NotLoggedInException;
import com.austria.logistics.models.UserImpl;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class ShowUsersTest {
    Repository repository;
    Command register;
    Command showUsers;
    User userCustomer;
    User userManager;
    List<String> userCustomerParameters;
    List<String> userManagerParameters;

    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
        register = new Register(repository);
        showUsers = new ShowUsers(repository);
        userCustomerParameters = List.of("Test", "Test", "Test", "Test", "test@test.bg", UserRole.CUSTOMER.toString());
        userManagerParameters = List.of("Test1", "Test", "Test", "Test", "test1@test.bg", UserRole.MANAGER.toString());
        userCustomer = new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.CUSTOMER);
        userManager = new UserImpl("Test1", "Test", "Test", "Test", "test1@test.bg", UserRole.MANAGER);
        register.execute(userCustomerParameters);
        repository.logout();
        register.execute(userManagerParameters);
    }

    @Test
    void executeCommand_Should_Return_Error_When_Not_LoggedIn() {
        //Act,Assert
        Assertions.assertEquals("You are not logged in! Please login first!", showUsers.execute(List.of()));
    }

    @Test
    void executeCommand_Should_Throw_Error_When_Not_LoggedIn_As_Manager() {
        //Arrange
        repository.login(userCustomer);
        //Act,Assert
        Assertions.assertThrows(NotLoggedInException.class, () -> showUsers.execute(List.of()));
    }

    @Test
    void executeCommand_Should_Return_Users() {
        //Arrange
        repository.login(userManager);
        String expected = "Username: Test, First Name: Test, Last Name: Test, Password: Test, Email: test@test.bg, User Role: Customer\n" +
                "Username: Test1, First Name: Test, Last Name: Test, Password: Test, Email: test1@test.bg, User Role: Manager\n";
        //Act,Assert
        Assertions.assertEquals(expected.replace("\r\n", "\n").replace("\r", "\n").trim(),
                showUsers.execute(List.of()).replace("\r\n", "\n").replace("\r", "\n").trim());
    }
}