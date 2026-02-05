package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.assignCommands.AssignTruck;
import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.commands.creationCommands.CreateRoute;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.models.enums.TruckType;
import com.austria.logistics.models.vehicles.TruckImpl;
import com.austria.logistics.models.vehicles.contracts.Truck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShowRouteTest {
    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026,1,24,12,0);

    private Repository repository;
    private  Command createRouteCommand;
    private List<String> parameters;
    private int id;
    private Command showRoute;
    private Route route;
    private Truck truck;
    @BeforeEach
    void before(){
        repository = new RepositoryImpl();
        truck = new TruckImpl(1012, TruckType.MAN);
        showRoute = new ShowRoute(repository);
        createRouteCommand = new CreateRoute(repository);
        createRouteCommand.execute(List.of());

        route = this.repository.getRoutes().get(0);
        id = route.getId();

        parameters = List.of(String.valueOf(id));
    }

    @Test
    void showRouteCommand_Should_Return_NoAssigned_Trucks_And_NoAdded_Locations() {
        //Arrange
        String expected =
                "Current schedule for route with id 1:\n" +
                "No assigned truck to the route.\n" +
                "No locations added to the route yet.\n";

        //Act,Assert
        Assertions.assertEquals(expected, showRoute.execute(parameters));
    }
    @Test
    void showRouteCommand_Should_Return_Locations() {
        //Arrange
        route.addFirstLocationToRoute(Locations.SYD, FIXED_TIME);
        String expected =
                "Current schedule for route with id 1:\n" +
                        "No assigned truck to the route.\n" +
                        "City: Sydney, Scheduled time: Jan 24 12:00\n";

        //Act,Assert
        Assertions.assertEquals(expected, showRoute.execute(parameters));
    }

    @Test
    void showRouteCommand_Should_Return_Assigned_Truck() {
        //Arrange
        this.repository.assignTruckToRoute(truck,route);
        route.addFirstLocationToRoute(Locations.SYD, FIXED_TIME);
        String expected =
                "Current schedule for route with id 1:\n" +
                        "The route has assigned truck Man with id 1012.\n" +
                        "City: Sydney, Scheduled time: Jan 24 12:00\n";

        //Act,Assert
        Assertions.assertEquals(expected, showRoute.execute(parameters));
    }
    @Test
    void showRouteCommand_Should_Return_Error_When_ArgumentsAreInvalid() {
        //Arrange
        parameters = List.of("Test", "Test");
        String expected = "Invalid number of arguments. Expected: 1, Received: 2.";
        //Act,Assert
        Assertions.assertEquals(expected,showRoute.execute(parameters));
    }
}