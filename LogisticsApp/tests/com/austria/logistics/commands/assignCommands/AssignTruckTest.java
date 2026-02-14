package com.austria.logistics.commands.assignCommands;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.commands.creationCommands.CreateRoute;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.*;
import com.austria.logistics.models.UserImpl;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.CityName;
import com.austria.logistics.models.enums.TruckType;
import com.austria.logistics.models.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;


class AssignTruckTest {
    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 1, 24, 12, 0);

    private Repository repository;
    private Command assignTruck;
    private Command createRoute;
    private Route route;
    private User user;

    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
        repository.login(new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.EMPLOYEE));
        assignTruck = new AssignTruck(repository);
        createRoute = new CreateRoute(repository);
        createRoute.execute(List.of());
        route = repository.getRoutes().get(0);

        route.addFirstLocationToRoute(CityName.BRI, FIXED_TIME);
        route.addLocationToRoute(CityName.ADL);
        route.addLocationToRoute(CityName.DAR);
    }

    @Test
    void execute_Should_Throw_Error_When_User_Not_LoggedIn() {
        //Arrange
        repository.logout();
        //Act,Assert
        Assertions.assertThrows(NotLoggedInException.class, () -> assignTruck.execute(List.of(String.valueOf(route.getId()))));
    }

    @Test
    void execute_Should_Throw_Error_When_User_Not_LoggedIn_AsEmployee() {
        //Arrange
        repository.logout();
        user = new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.CUSTOMER);
        repository.login(user);
        //Act,Assert
        Assertions.assertThrows(NotLoggedInException.class, () -> assignTruck.execute(List.of(String.valueOf(route.getId()))));
    }

    @Test
    void execute_Should_Throw_Error_When_ArgumentsCount_IsInvalid() {
        //Act,Assert
        Assertions.assertThrows(InvalidValueException.class, () -> assignTruck.execute(List.of(String.valueOf(route.getId()))));
    }

    @Test
    void execute_Should_Throw_Error_When_Arguments_AreInvalid() {
        //Act,Assert
        Assertions.assertAll(
                () -> Assertions.assertThrows(InvalidValueException.class, () -> assignTruck.execute(List.of("asd", String.valueOf(route.getId())))),
                () -> Assertions.assertThrows(ElementNotFoundException.class, () -> assignTruck.execute(List.of(String.valueOf(2), TruckType.MAN.getDisplayName()))),
                () -> Assertions.assertThrows(InvalidTruckTypeException.class, () -> assignTruck.execute(List.of(String.valueOf(route.getId()), "TEST")))
        );
    }

    @Test
    void execute_Should_Throw_Error_When_The_Route_IsEmpty() {
        //Arrange
        createRoute.execute(List.of());
        //Act,Assert
        Assertions.assertThrows(RouteIsEmptyException.class, () ->
                assignTruck.execute(List.of(String.valueOf(2),
                        TruckType.MAN.getDisplayName())));
    }

    @Test
    void execute_Should_Return_Error_When_Route_Already_Has_Assigned_Truck() {
        //Act,Assert
        Assertions.assertEquals("Truck Man with id 1011 was assigned to route with id 1!",
                assignTruck.execute(List.of(String.valueOf(route.getId()),
                        TruckType.MAN.getDisplayName())));
    }

    @Test
    void execute_Should_Throw_Error_When_NoAvailable_Trucks_Left() {
        //Arrange
        this.repository.getTrucks()
                .forEach(truck -> {
                    if (truck.getTruckType() == TruckType.MAN) {
                        truck.assign();
                    }
                });
        //Act,Assert
        Assertions.assertThrows(NoAvailableTruckException.class, () ->
                assignTruck.execute(List.of(String.valueOf(route.getId()),
                        TruckType.MAN.getDisplayName())));
    }

    @Test
    void execute_Should_Assign_Truck_To_Route() {
        //Act,Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals("Truck Man with id 1011 was assigned to route with id 1!", assignTruck.execute(List.of(String.valueOf(route.getId()), TruckType.MAN.getDisplayName()))),
                () -> Assertions.assertEquals(1, repository.findElementById(repository.getTrucks(), 1011).getAssignedRoute().getId()),
                () -> Assertions.assertEquals(1011, repository.getRoutes().get(0).getAssignedTruck().getId())
        );
    }

}