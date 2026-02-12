package com.austria.logistics.commands.unassignCommands;

import com.austria.logistics.commands.assignCommands.AssignLocation;
import com.austria.logistics.commands.assignCommands.AssignPackage;
import com.austria.logistics.commands.assignCommands.AssignTruck;
import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.commands.creationCommands.CreatePackage;
import com.austria.logistics.commands.creationCommands.CreateRoute;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.UserImpl;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class UnassignPackageTest {
    private Repository repository;
    private Command createRoute;
    private Command assignLocation;
    private Command assignTruck;
    private Command createPackage;
    private Command assignPackage;
    private Command unassignPackage;
    private User user;
    private Route route;


    @BeforeEach
    void setUp(){
        repository = new RepositoryImpl();
        user = new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.EMPLOYEE);
        repository.login(user);
        createRoute = new CreateRoute(repository);
        assignLocation = new AssignLocation(repository);
        assignTruck = new AssignTruck(repository);
        createPackage = new CreatePackage(repository);
        assignPackage = new AssignPackage(repository);
        unassignPackage = new UnassignPackage(repository);
        createRoute.execute(List.of());
        route = repository.getRoutes().get(0);
        assignLocation.execute(List.of("1","Sydney","Feb","20","13:00"));
        assignLocation.execute(List.of("1","Darwin"));
        assignTruck.execute(List.of("1", "Man"));
        createPackage.execute(List.of("Brisbane", "Adelaide", "40", "test@test.com"));
        assignPackage.execute(List.of("2","1011"));

    }

    @Test
    void executeCommand_Should_Return_Error_When_Not_LoggedIn() {
        //Arrange
        repository.logout();
        //Act,Assert
        Assertions.assertEquals("You are not logged in! Please login first!", unassignPackage.execute(List.of()));
    }
    @Test
    void executeCommand_Should_Return_Error_When_Not_LoggedIn_As_Employee_Or_Manager() {
        //Arrange
        repository.logout();
        repository.login(new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.CUSTOMER));
        //Act,Assert
        Assertions.assertEquals("You are not logged in as manager or employee!", unassignPackage.execute(List.of()));
    }

    @Test
    void executeCommand_Should_Return_Error_When_ArgumentCount_IsInvalid() {
        //Act,Assert
        Assertions.assertEquals("Invalid number of arguments. Expected: 1, Received: 0.", unassignPackage.execute(List.of()));
    }

    @Test
    void executeCommand_Should_Return_Error_When_ArgumentValue_IsInvalid() {
        //Act,Assert
        Assertions.assertEquals("Package id has to be valid integer.", unassignPackage.execute(List.of("asd")));
    }

    @Test
    void executeCommand_Should_Return_Error_When_Package_Is_NotFound() {
        //Act,Assert
        Assertions.assertEquals("No record with id 23 in the repository", unassignPackage.execute(List.of("23")));
    }

    @Test
    void executeCommand_Should_Return_Error_When_Package_Is_NotAssigned() {
        //Arrange
        createPackage.execute(List.of("Brisbane", "Adelaide", "60", "test@test.com"));
        //Act,Assert
        Assertions.assertEquals("Package with id 3 is not assigned to any truck yet!", unassignPackage.execute(List.of("3")));
    }

    @Test
    void executeCommand_Should_Unassign_Package() {
        //Act
        unassignPackage.execute(List.of("2"));
        // Assert
        Assertions.assertAll(
                () -> Assertions.assertFalse(repository.getPackages().get(0).isAssigned()),
                () -> Assertions.assertNull(repository.getPackages().get(0).getAssignedTruck()),
                () -> Assertions.assertFalse(repository.findElementById(repository.getTrucks(),1011).getAssignedPackagesIdList().contains(2))
        );

    }
}