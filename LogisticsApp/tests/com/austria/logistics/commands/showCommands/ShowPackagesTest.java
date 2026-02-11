package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.assignCommands.AssignPackage;
import com.austria.logistics.commands.assignCommands.AssignTruck;
import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.commands.creationCommands.CreateRoute;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.UserImpl;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.models.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class ShowPackagesTest {
    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 1, 24, 12, 0);

    private Repository repository;
    private Command showPackages;
    private List<String> parameters;
    private Command createRoute;
    private Command assignTruck;
    private Command assignPackage;

    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
        repository.login(new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.EMPLOYEE));
        showPackages = new ShowPackages(repository);
        parameters = List.of();
        createRoute = new CreateRoute(repository);
        assignTruck = new AssignTruck(repository);
        assignPackage = new AssignPackage(repository);
    }

    @Test
    void execute_Should_Return_Error_When_User_Not_LoggedIn() {
        //Arrange
        repository.logout();
        String expected = "You are not logged in! Please login first!";

        //Act
        String result = showPackages.execute(parameters);

        //Assert
        Assertions.assertEquals(expected, result);
    }

    @Test
    void execute_Should_Return_Packages() {
        //Arrange
        createRoute.execute(parameters);

        Route route = repository.getRoutes().get(0);
        route.addFirstLocationToRoute(Locations.SYD, FIXED_TIME);
        route.addLocationToRoute(Locations.BRI);
        route.addLocationToRoute(Locations.ADL);
        route.addLocationToRoute(Locations.DAR);

        assignTruck.execute(List.of(String.valueOf(route.getId()), "Man"));
        this.repository.createPackage(Locations.SYD, Locations.BRI, 30, "test@test.com");
        this.repository.createPackage(Locations.ADL, Locations.DAR, 40, "test@test.bg");

        String expected = "Package with id 2, start location Sydney, end location Brisbane, weight 30, contact info test@test.com is assigned to truck Man with id 1011\n" +
                "Estimated arrival time is: Jan 24 22:27\n" +
                "Package with id 3, start location Adelaide, end location Darwin, weight 40, contact info test@test.bg is not assigned to a truck yet.\n";

        assignPackage.execute(List.of("2", "1011"));
        //Act

        String result = showPackages.execute(parameters);

        //Assert

        Assertions.assertEquals(expected, result);
    }

    @Test
    void execute_Should_Return_No_Packages_Created() {
        //Arrange
        String expected = "No packages in the repo created yet.\n";

        //Act

        String result = showPackages.execute(parameters);

        //Assert
        Assertions.assertEquals(expected, result);
    }
}