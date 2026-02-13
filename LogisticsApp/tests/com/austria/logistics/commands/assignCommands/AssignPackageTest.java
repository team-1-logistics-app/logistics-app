package com.austria.logistics.commands.assignCommands;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.commands.creationCommands.CreatePackage;
import com.austria.logistics.commands.creationCommands.CreateRoute;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.*;
import com.austria.logistics.models.UserImpl;
import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.models.vehicles.contracts.Truck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class AssignPackageTest {
    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 1, 24, 12, 0);
    private Repository repository;
    private Command assignPackage;
    private Command createPackage;
    private Command createRoute;
    private Command assignTruck;
    private Route route;
    private User user;

    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
        user = new UserImpl("Test","Test","Test","Test", "test@test.bg", UserRole.EMPLOYEE);
        repository.login(user);
        assignPackage = new AssignPackage(repository);
        createRoute = new CreateRoute(repository);
        createPackage = new CreatePackage(repository);
        assignTruck = new AssignTruck(repository);

        createRoute.execute(List.of());
        route = repository.getRoutes().get(0);

        route.addFirstLocationToRoute(Locations.BRI, FIXED_TIME);
        route.addLocationToRoute(Locations.ADL);
        route.addLocationToRoute(Locations.DAR);

        assignTruck.execute(List.of(String.valueOf(route.getId()), "Man"));
    }


    @Test
    void execute_Should_AssignPackage_To_Truck() {
        //Arrange
        createPackage.execute(List.of("Brisbane", "Adelaide", "40", "test@test.com"));
        Package pkg = repository.getPackages().get(0);
        Truck truck = this.repository.findElementById(this.repository.getTrucks(), 1011);

        //Act
        String commandOutput = assignPackage.execute(List.of(String.valueOf(pkg.getId()), String.valueOf(truck.getId())));

        // Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, truck.getAssignedPackagesIdList().get(0)),
                () -> Assertions.assertEquals(40, truck.getCurrentWeight()),
                () -> Assertions.assertEquals("Package with id 2 was assigned to truck Man with id 1011!", commandOutput)
        );
    }

    @Test
    void execute_Should_Return_Error_When_User_Not_LoggedIn() {
        //Arrange
        repository.logout();
        //Act,Assert
        Assertions.assertEquals("You are not logged in! Please login first!", assignPackage.execute(List.of()));
    }

    @Test
    void execute_Should_Return_Error_When_User_Not_LoggedIn_AsEmployee() {
        //Arrange
        repository.logout();
        user = new UserImpl("Test","Test","Test","Test", "test@test.bg", UserRole.CUSTOMER);
        repository.login(user);
        //Act,Assert
        Assertions.assertEquals("You are not logged in as manager or employee!", assignPackage.execute(List.of()));
    }


    @Test
    void execute_Should_Throw_Error_When_ArgumentsCount_IsInvalid() {
        //Act,Assert
        Assertions.assertThrows(InvalidValueException.class, () -> assignPackage.execute(List.of()));
    }

    @Test
    void execute_Should_Throw_Error_When_Arguments_AreInvalid() {
        //Act,Assert
        Assertions.assertAll(
                () -> Assertions.assertThrows(InvalidValueException.class,() -> assignPackage.execute(List.of("asd", "12"))),
                () -> Assertions.assertThrows(InvalidValueException.class,() ->assignPackage.execute(List.of("1", "asd")))
        );

    }

    @Test
    void execute_Should_Throw_Error_When_Truck_IsNotAssigned_To_Route() {
        //Arrange
        createPackage.execute(List.of("Brisbane", "Adelaide", "40", "test@test.com"));
        Package pkg = repository.getPackages().get(0);
        Truck truck = this.repository.findElementById(this.repository.getTrucks(), 1012);
        //Act,Assert
        Assertions.assertThrows(TruckNotAssignedToRouteException.class, () -> assignPackage.execute(List.of(String.valueOf(pkg.getId()), String.valueOf(truck.getId()))));
    }

    @Test
    void execute_Should_Throw_Error_When_DepartLocation_Or_ArriveLocation_Location_IsNot_In_Route() {
        //Arrange
        createPackage.execute(List.of("Sydney", "Adelaide", "40", "test@test.com"));
        createPackage.execute(List.of("Adelaide", "Sydney", "40", "test@test.com"));
        Package pkg = repository.getPackages().get(0);
        Package pkg1 = repository.getPackages().get(1);
        Truck truck = this.repository.findElementById(this.repository.getTrucks(), 1011);
        //Act,Assert
        Assertions.assertAll(
                () -> Assertions.assertThrows(LocationNotFoundException.class,() -> assignPackage.execute(List.of(String.valueOf(pkg.getId()), String.valueOf(truck.getId())))),
                () -> Assertions.assertThrows(LocationNotFoundException.class,() -> assignPackage.execute(List.of(String.valueOf(pkg1.getId()), String.valueOf(truck.getId()))))
        );
    }

    @Test
    void execute_Should_Throw_Error_When_DepartLocation_And_ArriveLocation_Location_Have_NoPath_InRoute() {
        //Arrange
        createPackage.execute(List.of("Adelaide", "Adelaide", "40", "test@test.com"));
        createPackage.execute(List.of("Adelaide", "Brisbane", "40", "test@test.com"));
        createPackage.execute(List.of("Darwin", "Brisbane", "40", "test@test.com"));
        Package pkg = repository.getPackages().get(0);
        Package pkg1 = repository.getPackages().get(1);
        Package pkg2 = repository.getPackages().get(2);
        Truck truck = this.repository.findElementById(this.repository.getTrucks(), 1011);
        //Act,Assert
        Assertions.assertAll(
                () -> Assertions.assertThrows(NoPathException.class, () -> assignPackage.execute(List.of(String.valueOf(pkg.getId()), String.valueOf(truck.getId())))),
                () -> Assertions.assertThrows(NoPathException.class, () -> assignPackage.execute(List.of(String.valueOf(pkg1.getId()), String.valueOf(truck.getId())))),
                () -> Assertions.assertThrows(NoPathException.class, () -> assignPackage.execute(List.of(String.valueOf(pkg2.getId()), String.valueOf(truck.getId()))))
        );
    }

    @Test
    void execute_Should_Throw_Error_When_MaxCapacity_isReached() {
        //Arrange
        createPackage.execute(List.of("Brisbane", "Adelaide", "90000", "test@test.com"));
        Package pkg = repository.getPackages().get(0);
        Truck truck = this.repository.findElementById(this.repository.getTrucks(), 1011);
        //Act,Assert
        Assertions.assertThrows(MaxCapacityReachedException.class, () -> assignPackage.execute(List.of(String.valueOf(pkg.getId()), String.valueOf(truck.getId()))));
    }
}