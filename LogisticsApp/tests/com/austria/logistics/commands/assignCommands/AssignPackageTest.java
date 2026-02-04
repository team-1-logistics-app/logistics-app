package com.austria.logistics.commands.assignCommands;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.models.vehicles.contracts.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AssignPackageTest {

    private Repository repository;
    private AssignPackage assignPackageCommand;

    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
        assignPackageCommand = new AssignPackage(repository);
    }

    // ==================== Validation Tests ====================

    @Test
    void execute_Should_ReturnError_When_NoArgumentsProvided() {
        // Arrange
        List<String> parameters = new ArrayList<>();

        // Act
        String result = assignPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE, 2, 0);
        assertEquals(expectedMessage, result);
    }

    @Test
    void execute_Should_ReturnError_When_OnlyOneArgumentProvided() {
        // Arrange
        List<String> parameters = Arrays.asList("1");

        // Act
        String result = assignPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE, 2, 1);
        assertEquals(expectedMessage, result);
    }

    @Test
    void execute_Should_ReturnError_When_TooManyArgumentsProvided() {
        // Arrange
        List<String> parameters = Arrays.asList("1", "1001", "extra");

        // Act
        String result = assignPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE, 2, 3);
        assertEquals(expectedMessage, result);
    }

    @Test
    void execute_Should_ReturnError_When_PackageIdIsNotInteger() {
        // Arrange
        List<String> parameters = Arrays.asList("invalid", "1001");

        // Act
        String result = assignPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.VALUE_INVALID_FORMAT_MESSAGE, "Package id");
        assertEquals(expectedMessage, result);
    }

    @Test
    void execute_Should_ReturnError_When_TruckIdIsNotInteger() {
        // Arrange
        Package pkg = repository.createPackage(Locations.SYD, Locations.MEL, 100, "test@test.com");
        List<String> parameters = Arrays.asList(String.valueOf(pkg.getId()), "invalid");

        // Act
        String result = assignPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.VALUE_INVALID_FORMAT_MESSAGE, "Truck id");
        assertEquals(expectedMessage, result);
    }

    // ==================== Package Not Found Tests ====================

    @Test
    void execute_Should_ReturnError_When_PackageNotFound() {
        // Arrange
        int nonExistingPackageId = 99999;
        Truck truck = repository.getTrucks().get(0);
        List<String> parameters = Arrays.asList(String.valueOf(nonExistingPackageId), String.valueOf(truck.getId()));

        // Act
        String result = assignPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.ELEMENT_NOT_FOUND_MESSAGE, nonExistingPackageId);
        assertEquals(expectedMessage, result);
    }

    // ==================== Truck Not Found Tests ====================

    @Test
    void execute_Should_ReturnError_When_TruckNotFound() {
        // Arrange
        Package pkg = repository.createPackage(Locations.SYD, Locations.MEL, 100, "test@test.com");
        int nonExistingTruckId = 99999;
        List<String> parameters = Arrays.asList(String.valueOf(pkg.getId()), String.valueOf(nonExistingTruckId));

        // Act
        String result = assignPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.ELEMENT_NOT_FOUND_MESSAGE, nonExistingTruckId);
        assertEquals(expectedMessage, result);
    }

    // ==================== Max Capacity Tests ====================

    @Test
    void execute_Should_ReturnError_When_TruckMaxCapacityExceeded() {
        // Arrange
        // ACTROS truck has capacity of 26000
        Truck actrosTruck = repository.getTrucks().stream()
                .filter(t -> t.getTruckType().getDisplayName().equals("Actros"))
                .findFirst()
                .orElseThrow();
        
        // Create a package that exceeds capacity
        Package pkg = repository.createPackage(Locations.SYD, Locations.MEL, 30000, "test@test.com");
        List<String> parameters = Arrays.asList(String.valueOf(pkg.getId()), String.valueOf(actrosTruck.getId()));

        // Act
        String result = assignPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.TRUCK_MAXCAPACITY_REACHED_MESSAGE, 
                actrosTruck.getTruckType().getDisplayName(), actrosTruck.getId());
        assertEquals(expectedMessage, result);
    }

    @Test
    void execute_Should_ReturnError_When_TruckMaxCapacityExceededWithMultiplePackages() {
        // Arrange
        // ACTROS truck has capacity of 26000
        Truck actrosTruck = repository.getTrucks().stream()
                .filter(t -> t.getTruckType().getDisplayName().equals("Actros"))
                .findFirst()
                .orElseThrow();
        
        // First package uses most of the capacity
        Package pkg1 = repository.createPackage(Locations.SYD, Locations.MEL, 25000, "test1@test.com");
        List<String> params1 = Arrays.asList(String.valueOf(pkg1.getId()), String.valueOf(actrosTruck.getId()));
        assignPackageCommand.execute(params1);
        
        // Second package would exceed capacity
        Package pkg2 = repository.createPackage(Locations.BRI, Locations.DAR, 2000, "test2@test.com");
        List<String> params2 = Arrays.asList(String.valueOf(pkg2.getId()), String.valueOf(actrosTruck.getId()));

        // Act
        String result = assignPackageCommand.execute(params2);

        // Assert
        String expectedMessage = String.format(Constants.TRUCK_MAXCAPACITY_REACHED_MESSAGE, 
                actrosTruck.getTruckType().getDisplayName(), actrosTruck.getId());
        assertEquals(expectedMessage, result);
    }

    // ==================== Success Tests ====================

    @Test
    void execute_Should_AssignPackage_When_ArgumentsAreValid() {
        // Arrange
        Package pkg = repository.createPackage(Locations.SYD, Locations.MEL, 100, "test@test.com");
        Truck truck = repository.getTrucks().get(0);
        List<String> parameters = Arrays.asList(String.valueOf(pkg.getId()), String.valueOf(truck.getId()));

        // Act
        String result = assignPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.PACKAGE_ASSIGNED_MESSAGE, 
                pkg.getId(), truck.getTruckType().getDisplayName(), truck.getId());
        assertEquals(expectedMessage, result);
    }

    @Test
    void execute_Should_SetTruckOnPackage_When_Assigned() {
        // Arrange
        Package pkg = repository.createPackage(Locations.SYD, Locations.MEL, 100, "test@test.com");
        Truck truck = repository.getTrucks().get(0);
        List<String> parameters = Arrays.asList(String.valueOf(pkg.getId()), String.valueOf(truck.getId()));

        // Act
        assignPackageCommand.execute(parameters);

        // Assert
        assertEquals(truck, pkg.getAssignedToTruck());
    }

    @Test
    void execute_Should_AddPackageIdToTruck_When_Assigned() {
        // Arrange
        Package pkg = repository.createPackage(Locations.SYD, Locations.MEL, 100, "test@test.com");
        Truck truck = repository.getTrucks().get(0);
        List<String> parameters = Arrays.asList(String.valueOf(pkg.getId()), String.valueOf(truck.getId()));

        // Act
        assignPackageCommand.execute(parameters);

        // Assert
        assertTrue(truck.getAssignedPackagesIdList().contains(pkg.getId()));
    }

    @Test
    void execute_Should_AssignMultiplePackages_ToSameTruck() {
        // Arrange
        Package pkg1 = repository.createPackage(Locations.SYD, Locations.MEL, 100, "test1@test.com");
        Package pkg2 = repository.createPackage(Locations.BRI, Locations.DAR, 200, "test2@test.com");
        Truck truck = repository.getTrucks().get(0);
        
        List<String> params1 = Arrays.asList(String.valueOf(pkg1.getId()), String.valueOf(truck.getId()));
        List<String> params2 = Arrays.asList(String.valueOf(pkg2.getId()), String.valueOf(truck.getId()));

        // Act
        String result1 = assignPackageCommand.execute(params1);
        String result2 = assignPackageCommand.execute(params2);

        // Assert
        assertAll(
                () -> assertTrue(result1.contains("was assigned")),
                () -> assertTrue(result2.contains("was assigned")),
                () -> assertEquals(2, truck.getAssignedPackagesIdList().size())
        );
    }

    @Test
    void execute_Should_AssignPackage_ToDifferentTruckTypes() {
        // Arrange
        Truck scaniaTruck = repository.getTrucks().stream()
                .filter(t -> t.getTruckType().getDisplayName().equals("Scania"))
                .findFirst()
                .orElseThrow();
        Truck manTruck = repository.getTrucks().stream()
                .filter(t -> t.getTruckType().getDisplayName().equals("Man"))
                .findFirst()
                .orElseThrow();
        Truck actrosTruck = repository.getTrucks().stream()
                .filter(t -> t.getTruckType().getDisplayName().equals("Actros"))
                .findFirst()
                .orElseThrow();

        Package pkg1 = repository.createPackage(Locations.SYD, Locations.MEL, 100, "test1@test.com");
        Package pkg2 = repository.createPackage(Locations.BRI, Locations.DAR, 200, "test2@test.com");
        Package pkg3 = repository.createPackage(Locations.ADL, Locations.PER, 300, "test3@test.com");

        // Act
        String result1 = assignPackageCommand.execute(Arrays.asList(String.valueOf(pkg1.getId()), String.valueOf(scaniaTruck.getId())));
        String result2 = assignPackageCommand.execute(Arrays.asList(String.valueOf(pkg2.getId()), String.valueOf(manTruck.getId())));
        String result3 = assignPackageCommand.execute(Arrays.asList(String.valueOf(pkg3.getId()), String.valueOf(actrosTruck.getId())));

        // Assert
        assertAll(
                () -> assertTrue(result1.contains("Scania")),
                () -> assertTrue(result2.contains("Man")),
                () -> assertTrue(result3.contains("Actros"))
        );
    }

    @Test
    void execute_Should_AssignPackage_AtExactMaxCapacity() {
        // Arrange
        // ACTROS truck has capacity of 26000
        Truck actrosTruck = repository.getTrucks().stream()
                .filter(t -> t.getTruckType().getDisplayName().equals("Actros"))
                .findFirst()
                .orElseThrow();
        
        Package pkg = repository.createPackage(Locations.SYD, Locations.MEL, 26000, "test@test.com");
        List<String> parameters = Arrays.asList(String.valueOf(pkg.getId()), String.valueOf(actrosTruck.getId()));

        // Act
        String result = assignPackageCommand.execute(parameters);

        // Assert
        assertTrue(result.contains("was assigned"));
    }

    // ==================== Constant Verification Tests ====================

    @Test
    void expectedNumberOfArguments_Should_BeTwo() {
        // Assert
        assertEquals(2, AssignPackage.EXPECTED_NUMBER_OF_ARGUMENTS);
    }
}
