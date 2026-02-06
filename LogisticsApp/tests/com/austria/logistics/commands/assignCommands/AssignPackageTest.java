package com.austria.logistics.commands.assignCommands;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.commands.creationCommands.CreatePackage;
import com.austria.logistics.commands.creationCommands.CreateRoute;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.models.enums.TruckType;
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
    private  Route route;

    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
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
        Truck truck = this.repository.findElementById(this.repository.getTrucks(),1011);

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
    void execute_Should_Return_Error_When_ArgumentsCount_IsInvalid(){
        //Act,Assert
        Assertions.assertEquals("Invalid number of arguments. Expected: 2, Received: 0.", assignPackage.execute(List.of()));
    }

    @Test
    void execute_Should_Return_Error_When_Arguments_AreInvalid(){
        //Act,Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals("Package id has to be valid integer.", assignPackage.execute(List.of("asd","12"))),
                () -> Assertions.assertEquals("Truck id has to be valid integer.", assignPackage.execute(List.of("1","asd")))
        );

    }

    @Test
    void execute_Should_Return_Error_When_Truck_IsNotAssigned_To_Route(){
        //Arrange
        createPackage.execute(List.of("Brisbane", "Adelaide", "40", "test@test.com"));
        Package pkg = repository.getPackages().get(0);
        Truck truck = this.repository.findElementById(this.repository.getTrucks(),1012);

        //Act
        String commandOutput = assignPackage.execute(List.of(String.valueOf(pkg.getId()), String.valueOf(truck.getId())));

        //Assert
        Assertions.assertEquals("Truck Man with id 1012 is not assigned to route yet, assign it to route before assigning packages to it.",commandOutput);
    }
    @Test
    void execute_Should_Return_Error_When_DepartLocation_Or_ArriveLocation_Location_IsNot_In_Route(){
        //Arrange
        createPackage.execute(List.of("Sydney", "Adelaide", "40", "test@test.com"));
        createPackage.execute(List.of("Adelaide", "Sydney", "40", "test@test.com"));
        Package pkg = repository.getPackages().get(0);
        Package pkg1 = repository.getPackages().get(1);
        Truck truck = this.repository.findElementById(this.repository.getTrucks(),1011);

        //Act
        String commandOutput = assignPackage.execute(List.of(String.valueOf(pkg.getId()), String.valueOf(truck.getId())));
        String commandOutput1 = assignPackage.execute(List.of(String.valueOf(pkg1.getId()),String.valueOf(truck.getId())));

        //Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals("Sydney is not in the route.",commandOutput),
                () -> Assertions.assertEquals("Sydney is not in the route.",commandOutput1)
        );
    }

    @Test
    void execute_Should_Return_Error_When_DepartLocation_And_ArriveLocation_Location_Have_NoPath_InRoute(){
        //Arrange
        createPackage.execute(List.of("Adelaide", "Adelaide", "40", "test@test.com"));
        createPackage.execute(List.of("Adelaide", "Brisbane", "40", "test@test.com"));
        createPackage.execute(List.of("Darwin", "Brisbane", "40", "test@test.com"));
        Package pkg = repository.getPackages().get(0);
        Package pkg1 = repository.getPackages().get(1);
        Package pkg2 = repository.getPackages().get(2);
        Truck truck = this.repository.findElementById(this.repository.getTrucks(),1011);

        //Act
        String commandOutput = assignPackage.execute(List.of(String.valueOf(pkg.getId()), String.valueOf(truck.getId())));
        String commandOutput1 = assignPackage.execute(List.of(String.valueOf(pkg1.getId()),String.valueOf(truck.getId())));
        String commandOutput2 = assignPackage.execute(List.of(String.valueOf(pkg2.getId()),String.valueOf(truck.getId())));

        //Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals("Package with id 2 cannot be assigned to route with id 1 because the route doesn't contain path from Adelaide to Adelaide.",commandOutput),
                () -> Assertions.assertEquals("Package with id 3 cannot be assigned to route with id 1 because the route doesn't contain path from Adelaide to Brisbane.",commandOutput1),
                () -> Assertions.assertEquals("Package with id 4 cannot be assigned to route with id 1 because the route doesn't contain path from Darwin to Brisbane.",commandOutput2)
        );
    }
}