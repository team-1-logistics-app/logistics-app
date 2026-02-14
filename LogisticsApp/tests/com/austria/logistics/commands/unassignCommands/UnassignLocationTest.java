package com.austria.logistics.commands.unassignCommands;

import com.austria.logistics.commands.assignCommands.AssignLocation;
import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.commands.creationCommands.CreateRoute;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.FailedToRemoveLocationException;
import com.austria.logistics.exceptions.InvalidLocationException;
import com.austria.logistics.exceptions.InvalidValueException;
import com.austria.logistics.exceptions.NotLoggedInException;
import com.austria.logistics.models.UserImpl;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


class UnassignLocationTest {
    private Repository repository;
    private Command createRoute;
    private Command assignLocation;
    private Command unassignLocation;
    private User user;

    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
        user = new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.EMPLOYEE);
        repository.login(user);
        createRoute = new CreateRoute(repository);
        assignLocation = new AssignLocation(repository);
        unassignLocation = new UnassignLocation(repository);
        createRoute.execute(List.of());
        assignLocation.execute(List.of("1", "Sydney", "Feb", "20", "13:00"));
        assignLocation.execute(List.of("1", "Darwin"));
    }

    @Test
    void executeCommand_Should_Return_Error_When_Not_LoggedIn() {
        //Arrange
        repository.logout();
        //Act,Assert
        Assertions.assertEquals("You are not logged in! Please login first!",unassignLocation.execute(List.of()));
    }

    @Test
    void executeCommand_Should_Throw_Error_When_Not_LoggedIn_As_Employee_Or_Manager() {
        //Arrange
        repository.logout();
        repository.login(new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.CUSTOMER));
        //Act,Assert
        Assertions.assertThrows(NotLoggedInException.class, () -> unassignLocation.execute(List.of()));
    }

    @Test
    void executeCommand_Should_Throw_Error_When_ArgumentCount_IsInvalid() {
        //Act,Assert
        Assertions.assertThrows(InvalidValueException.class, () -> unassignLocation.execute(List.of()));
    }

    @Test
    void executeCommand_Should_Throw_Error_When_ArgumentValue_IsInvalid() {
        //Act,Assert
        Assertions.assertAll(
                () -> Assertions.assertThrows(InvalidValueException.class,() -> unassignLocation.execute(List.of("asd","Darwin"))),
                () -> Assertions.assertThrows(InvalidLocationException.class,() -> unassignLocation.execute(List.of("1","asd")))
        );

    }

    @Test
    void executeCommand_Should_Throw_Error_When_Unassigning_startLocation_But_ThereAre_OtherLocation() {
        //Act,Assert
        Assertions.assertThrows(FailedToRemoveLocationException.class, () ->
                unassignLocation.execute(List.of("1","Sydney")));
    }

    @Test
    void executeCommand_Should_Unassign_Location() {
        //Act
        unassignLocation.execute(List.of("1","Darwin"));
        unassignLocation.execute(List.of("1","Sydney"));
        // Assert
        Assertions.assertEquals(0,repository.getRoutes().get(0).getRouteLocations().size());
    }


}