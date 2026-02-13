package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.assignCommands.AssignTruck;
import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.commands.creationCommands.CreateRoute;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.UserImpl;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.enums.CityName;
import com.austria.logistics.models.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class ShowTrucksTest {
    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 1, 24, 12, 0);
    private Repository repository;
    private Command showTrucks;

    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
        repository.login(new UserImpl("Test","Test","Test","Test","test@test.bg", UserRole.EMPLOYEE));
        Command createRoute = new CreateRoute(repository);
        createRoute.execute(List.of());

        Route route = repository.getRoutes().get(0);
        route.addFirstLocationToRoute(CityName.BRI, FIXED_TIME);
        route.addLocationToRoute(CityName.ADL);
        route.addLocationToRoute(CityName.BRI);

        Command assignTruck = new AssignTruck(repository);
        assignTruck.execute(List.of(String.valueOf(route.getId()), "Man"));

        showTrucks = new ShowTrucks(repository);
    }

    @Test
    void execute_Should_Return_Error_When_User_Not_LoggedIn() {
        //Arrange
        repository.logout();
        //Act
        String result = showTrucks.execute(List.of("Test"));
        //Assert
        Assertions.assertEquals("You are not logged in! Please login first!", result);
    }

    @Test
    void execute_Should_Return_Correct_Trucks_Information() {
        //Act
        String result = showTrucks.execute(List.of());
        //Assert
        Assertions.assertTrue(result.startsWith("Scania with id 1001 is not assigned."));
        Assertions.assertTrue(result.contains("Man with id 1011 is assigned to route with id 1, current weight is 0 kg and max capacity is 37000 kg"));
        Assertions.assertTrue(result.contains("Man with id 1012 is not assigned."));
        Assertions.assertTrue(result.contains("Actros with id 1040 is not assigned."));
        Assertions.assertEquals(40, result.split("\n").length);
    }
}