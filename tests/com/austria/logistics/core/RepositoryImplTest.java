package com.austria.logistics.core;

import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.ElementNotFoundException;
import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.models.enums.TruckType;
import com.austria.logistics.models.vehicles.contracts.Truck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryImplTest {

    private Repository repository;

    @BeforeEach
    void before() {
        repository = new RepositoryImpl();
    }

    @Test
    public void constructor_Should_InitializeTrucks_When_RepositoryIsCreated() {
        // Arrange, Act
        List<Truck> trucks = repository.getTrucks();

        // Assert
        assertEquals(40, trucks.size());
    }

    @Test
    public void constructor_Should_InitializeCorrectNumberOfScaniaTrucks() {
        // Arrange, Act
        List<Truck> trucks = repository.getTrucks();
        long scaniaCount = trucks.stream()
                .filter(t -> t.getTruckType() == TruckType.SCANIA)
                .count();

        // Assert
        assertEquals(10, scaniaCount);
    }

    @Test
    public void constructor_Should_InitializeCorrectNumberOfManTrucks() {
        // Arrange, Act
        List<Truck> trucks = repository.getTrucks();
        long manCount = trucks.stream()
                .filter(t -> t.getTruckType() == TruckType.MAN)
                .count();

        // Assert
        assertEquals(15, manCount);
    }

    @Test
    public void constructor_Should_InitializeCorrectNumberOfActrosTrucks() {
        // Arrange, Act
        List<Truck> trucks = repository.getTrucks();
        long actrosCount = trucks.stream()
                .filter(t -> t.getTruckType() == TruckType.ACTROS)
                .count();

        // Assert
        assertEquals(15, actrosCount);
    }

    @Test
    public void constructor_Should_InitializeEmptyRoutesList() {
        // Arrange, Act, Assert
        assertTrue(repository.getRoutes().isEmpty());
    }

    @Test
    public void constructor_Should_InitializeEmptyPackagesList() {
        // Arrange, Act, Assert
        assertTrue(repository.getPackages().isEmpty());
    }

    @Test
    public void getTrucks_Should_ReturnDefensiveCopy() {
        // Arrange
        List<Truck> trucks1 = repository.getTrucks();
        int originalSize = trucks1.size();

        // Act
        trucks1.clear();
        List<Truck> trucks2 = repository.getTrucks();

        // Assert
        assertEquals(originalSize, trucks2.size());
    }

    @Test
    public void getRoutes_Should_ReturnDefensiveCopy() {
        // Arrange
        repository.createRoute();
        List<Route> routes1 = repository.getRoutes();
        int originalSize = routes1.size();

        // Act
        routes1.clear();
        List<Route> routes2 = repository.getRoutes();

        // Assert
        assertEquals(originalSize, routes2.size());
    }

    @Test
    public void getPackages_Should_ReturnDefensiveCopy() {
        // Arrange
        repository.createPackage(Locations.SYD, Locations.MEL, 100, "Test Contact");
        List<Package> packages1 = repository.getPackages();
        int originalSize = packages1.size();

        // Act
        packages1.clear();
        List<Package> packages2 = repository.getPackages();

        // Assert
        assertEquals(originalSize, packages2.size());
    }

    @Test
    public void findElementById_Should_ReturnElement_When_ElementExists() {
        // Arrange
        List<Truck> trucks = repository.getTrucks();

        // Act
        Truck foundTruck = repository.findElementById(trucks, 1001);

        // Assert
        assertAll(
                () -> assertNotNull(foundTruck),
                () -> assertEquals(1001, foundTruck.getId())
        );
    }

    @Test
    public void findElementById_Should_ThrowException_When_ElementNotFound() {
        // Arrange
        List<Truck> trucks = repository.getTrucks();

        // Act, Assert
        Assertions.assertThrows(ElementNotFoundException.class,
                () -> repository.findElementById(trucks, 9999));
    }

    @Test
    public void findElementById_Should_FindRoute_When_RouteExists() {
        // Arrange
        Route createdRoute = repository.createRoute();
        List<Route> routes = repository.getRoutes();

        // Act
        Route foundRoute = repository.findElementById(routes, createdRoute.getId());

        // Assert
        assertEquals(createdRoute.getId(), foundRoute.getId());
    }

    @Test
    public void findElementById_Should_FindPackage_When_PackageExists() {
        // Arrange
        Package createdPackage = repository.createPackage(Locations.SYD, Locations.MEL, 50, "Contact");
        List<Package> packages = repository.getPackages();

        // Act
        Package foundPackage = repository.findElementById(packages, createdPackage.getId());

        // Assert
        assertEquals(createdPackage.getId(), foundPackage.getId());
    }

    @Test
    public void createRoute_Should_CreateRoute_And_AddToRepository() {
        // Arrange
        int initialSize = repository.getRoutes().size();

        // Act
        Route route = repository.createRoute();

        // Assert
        assertAll(
                () -> assertNotNull(route),
                () -> assertEquals(initialSize + 1, repository.getRoutes().size())
        );
    }

    @Test
    public void createRoute_Should_AssignUniqueId_ToEachRoute() {
        // Arrange, Act
        Route route1 = repository.createRoute();
        Route route2 = repository.createRoute();
        Route route3 = repository.createRoute();

        // Assert
        assertAll(
                () -> assertEquals(1, route1.getId()),
                () -> assertEquals(2, route2.getId()),
                () -> assertEquals(3, route3.getId())
        );
    }

    @Test
    public void createPackage_Should_CreatePackage_And_AddToRepository() {
        // Arrange
        int initialSize = repository.getPackages().size();

        // Act
        Package pkg = repository.createPackage(Locations.SYD, Locations.MEL, 45, "John Doe");

        // Assert
        assertAll(
                () -> assertNotNull(pkg),
                () -> assertEquals(initialSize + 1, repository.getPackages().size())
        );
    }

    @Test
    public void createPackage_Should_SetCorrectProperties() {
        // Arrange, Act
        Package pkg = repository.createPackage(Locations.BRI, Locations.PER, 100, "Jane Smith");

        // Assert
        assertAll(
                () -> assertEquals(Locations.BRI, pkg.getStartLocation()),
                () -> assertEquals(Locations.PER, pkg.getEndLocation()),
                () -> assertEquals(100, pkg.getWeight()),
                () -> assertEquals("Jane Smith", pkg.getContactInformation())
        );
    }

    @Test
    public void createPackage_Should_AssignUniqueId_ToEachPackage() {
        // Arrange, Act
        Package pkg1 = repository.createPackage(Locations.SYD, Locations.MEL, 10, "Contact 1");
        Package pkg2 = repository.createPackage(Locations.MEL, Locations.ADL, 20, "Contact 2");
        Package pkg3 = repository.createPackage(Locations.ADL, Locations.PER, 30, "Contact 3");

        // Assert
        assertAll(
                () -> assertEquals(1, pkg1.getId()),
                () -> assertEquals(2, pkg2.getId()),
                () -> assertEquals(3, pkg3.getId())
        );
    }

    @Test
    public void createRoute_And_createPackage_Should_ShareIdCounter() {
        // Arrange, Act
        Route route1 = repository.createRoute();
        Package pkg1 = repository.createPackage(Locations.SYD, Locations.MEL, 10, "Contact");
        Route route2 = repository.createRoute();

        // Assert
        assertAll(
                () -> assertEquals(1, route1.getId()),
                () -> assertEquals(2, pkg1.getId()),
                () -> assertEquals(3, route2.getId())
        );
    }
}
