package com.austria.logistics.commands.unassignCommands;

import com.austria.logistics.commands.assignCommands.AssignTruck;
import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.commands.creationCommands.CreateRoute;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.ElementNotFoundException;
import com.austria.logistics.exceptions.InvalidValueException;
import com.austria.logistics.exceptions.NotLoggedInException;
import com.austria.logistics.exceptions.TruckNotAssignedToRouteException;
import com.austria.logistics.models.UserImpl;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.CityName;
import com.austria.logistics.models.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;


class UnassignTruckTest {
    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 1, 24, 12, 0);
    private Repository repository;
    private Command createRoute;
    private Command assignTruck;
    private Command unassignTruck;
    private Route route;
    private User user;

    @BeforeEach
    void setUp() {
        user = new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.EMPLOYEE);
        repository = new RepositoryImpl();
        assignTruck = new AssignTruck(repository);
        unassignTruck = new UnassignTruck(repository);
        repository.login(user);
        assignTruck = new AssignTruck(repository);
        createRoute = new CreateRoute(repository);
        createRoute.execute(List.of());
        route = repository.getRoutes().get(0);

        route.addFirstLocationToRoute(CityName.BRI, FIXED_TIME);
        route.addLocationToRoute(CityName.ADL);
        route.addLocationToRoute(CityName.DAR);
        assignTruck.execute(List.of("1", "Man"));

    }

    @Test
    void executeCommand_Should_Return_Error_When_User_Is_Not_LoggedIn() {
        //Arrange
        repository.logout();
        //Act,Assert
        Assertions.assertEquals("You are not logged in! Please login first!",
                unassignTruck.execute(List.of("1011", "1")));
    }

    @Test
    void executeCommand_Should_Throw_Error_When_User_Is_Not_LoggedIn_As_Employee_Or_Manager() {
        //Arrange
        repository.logout();
        repository.login(new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.CUSTOMER));
        //Act,Assert
        Assertions.assertThrows(NotLoggedInException.class, () ->
                unassignTruck.execute(List.of("1011", "1")));
    }

    @Test
    void executeCommand_Should_Throw_Error_When_ArgumentCount_IsInvalid() {
        //Arrange
        repository.login(user);
        //Act,Assert
        Assertions.assertThrows(InvalidValueException.class, () ->
                unassignTruck.execute(List.of("1011")));
    }

    @Test
    void executeCommand_Should_Throw_Error_When_ArgumentValue_IsInvalid() {
        //Arrange
        repository.login(user);
        //Act,Assert
        Assertions.assertAll(
                () -> Assertions.assertThrows(InvalidValueException.class, () ->
                        unassignTruck.execute(List.of("asd", "1"))),
                () -> Assertions.assertThrows(InvalidValueException.class, () ->
                        unassignTruck.execute(List.of("1011", "asd")))
        );
    }

    @Test
    void executeCommand_Should_Return_Error_When_Ids_Are_NotFound() {
        //Arrange
        repository.login(user);
        //Act,Assert
        Assertions.assertAll(
                () -> Assertions.assertThrows(ElementNotFoundException.class, () ->
                        unassignTruck.execute(List.of("10123", "1"))),
                () -> Assertions.assertThrows(ElementNotFoundException.class, () ->
                        unassignTruck.execute(List.of("1011", "123123")))
        );
    }

    @Test
    void executeCommand_Should_Throw_Error_When_Truck_Is_NotAssigned() {
        //Arrange
        repository.login(user);
        //Act,Assert
        Assertions.assertThrows(TruckNotAssignedToRouteException.class, () ->
                unassignTruck.execute(List.of("1012", "1")));

    }

    @Test
    void executeCommand_Should_Unassign_Truck() {
        //Arrange
        repository.login(user);
        //Act,Assert
        Assertions.assertEquals("Truck Man with id 1011 is successfully unassigned to route with id 1",
                unassignTruck.execute(List.of("1011", "1")));

    }

}