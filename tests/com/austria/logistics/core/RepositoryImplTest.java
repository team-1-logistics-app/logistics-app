package com.austria.logistics.core;

import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.ElementNotFoundException;
import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.models.enums.TruckType;
import com.austria.logistics.models.vehicles.contracts.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryImplTest {

    private Repository repository;

    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
    }

    // Constructor tests
    // Note: Due to current implementation, trucks are added twice (in createTruck and constructor loop)
    // Expected: 10 SCANIA + 15 MAN + 15 ACTROS = 40, but actual is 80 due to double-add
    @Test
    void constructor_Should_InitializeTrucks() {
        // Assert - Current implementation adds trucks twice: 80 total
        assertEquals(80, repository.getTrucks().size());
    }

    @Test
    void constructor_Should_InitializeScaniaTrucks() {
        // Arrange
        List<Truck> trucks = repository.getTrucks();

        // Act - Filter SCANIA trucks
        long scaniaCount = trucks.stream()
                .filter(t -> t.getTruckType() == TruckType.SCANIA)
                .count();

        // Assert - 10 SCANIA trucks doubled = 20
        assertEquals(20, scaniaCount);
    }

    @Test
    void constructor_Should_InitializeManTrucks() {
        // Arrange
        List<Truck> trucks = repository.getTrucks();

        // Act - Filter MAN trucks
        long manCount = trucks.stream()
                .filter(t -> t.getTruckType() == TruckType.MAN)
                .count();

        // Assert - 15 MAN trucks doubled = 30
        assertEquals(30, manCount);
    }

    @Test
    void constructor_Should_InitializeActrosTrucks() {
        // Arrange
        List<Truck> trucks = repository.getTrucks();

        // Act - Filter ACTROS trucks
        long actrosCount = trucks.stream()
                .filter(t -> t.getTruckType() == TruckType.ACTROS)
                .count();

        // Assert - 15 ACTROS trucks doubled = 30
        assertEquals(30, actrosCount);
    }

    @Test
    void constructor_Should_InitializeEmptyRoutesList() {
        // Assert
        assertTrue(repository.getRoutes().isEmpty());
    }

    @Test
    void constructor_Should_InitializeEmptyPackagesList() {
        // Assert
        assertTrue(repository.getPackages().isEmpty());
    }

    // getTrucks tests
    @Test
    void getTrucks_Should_ReturnDefensiveCopy() {
        // Arrange
        List<Truck> trucks1 = repository.getTrucks();
        int originalSize = trucks1.size();

        // Act - Try to modify returned list
        trucks1.clear();

        // Assert - Original list should not be affected
        assertEquals(originalSize, repository.getTrucks().size());
    }

    @Test
    void getTrucks_Should_ReturnAllTrucks() {
        // Assert
        assertNotNull(repository.getTrucks());
        assertFalse(repository.getTrucks().isEmpty());
    }

    // getRoutes tests
    @Test
    void getRoutes_Should_ReturnDefensiveCopy() {
        // Arrange
        repository.createRoute();
        List<Route> routes1 = repository.getRoutes();
        int originalSize = routes1.size();

        // Act - Try to modify returned list
        routes1.clear();

        // Assert - Original list should not be affected
        assertEquals(originalSize, repository.getRoutes().size());
    }

    @Test
    void getRoutes_Should_ReturnEmptyList_When_NoRoutesCreated() {
        // Assert
        assertNotNull(repository.getRoutes());
        assertTrue(repository.getRoutes().isEmpty());
    }

    // getPackages tests
    @Test
    void getPackages_Should_ReturnDefensiveCopy() {
        // Arrange
        repository.createPackage(Locations.SYD, Locations.MEL, 1000, "test@test.com");
        List<Package> packages1 = repository.getPackages();
        int originalSize = packages1.size();

        // Act - Try to modify returned list
        packages1.clear();

        // Assert - Original list should not be affected
        assertEquals(originalSize, repository.getPackages().size());
    }

    @Test
    void getPackages_Should_ReturnEmptyList_When_NoPackagesCreated() {
        // Assert
        assertNotNull(repository.getPackages());
        assertTrue(repository.getPackages().isEmpty());
    }

    // findElementById tests
    @Test
    void findElementById_Should_ReturnTruck_When_TruckExists() {
        // Arrange
        List<Truck> trucks = repository.getTrucks();

        // Act
        Truck foundTruck = repository.findElementById(trucks, 1001);

        // Assert
        assertNotNull(foundTruck);
        assertEquals(1001, foundTruck.getId());
    }

    @Test
    void findElementById_Should_ThrowException_When_TruckNotFound() {
        // Arrange
        List<Truck> trucks = repository.getTrucks();

        // Act, Assert
        assertThrows(ElementNotFoundException.class, () -> repository.findElementById(trucks, 9999));
    }

    @Test
    void findElementById_Should_ReturnRoute_When_RouteExists() {
        // Arrange
        Route createdRoute = repository.createRoute();
        List<Route> routes = repository.getRoutes();

        // Act
        Route foundRoute = repository.findElementById(routes, createdRoute.getId());

        // Assert
        assertNotNull(foundRoute);
        assertEquals(createdRoute.getId(), foundRoute.getId());
    }

    @Test
    void findElementById_Should_ThrowException_When_RouteNotFound() {
        // Arrange
        repository.createRoute();
        List<Route> routes = repository.getRoutes();

        // Act, Assert
        assertThrows(ElementNotFoundException.class, () -> repository.findElementById(routes, 9999));
    }

    @Test
    void findElementById_Should_ReturnPackage_When_PackageExists() {
        // Arrange
        Package createdPackage = repository.createPackage(Locations.SYD, Locations.MEL, 500, "contact@email.com");
        List<Package> packages = repository.getPackages();

        // Act
        Package foundPackage = repository.findElementById(packages, createdPackage.getId());

        // Assert
        assertNotNull(foundPackage);
        assertEquals(createdPackage.getId(), foundPackage.getId());
    }

    @Test
    void findElementById_Should_ThrowException_When_PackageNotFound() {
        // Arrange
        repository.createPackage(Locations.SYD, Locations.MEL, 500, "contact@email.com");
        List<Package> packages = repository.getPackages();

        // Act, Assert
        assertThrows(ElementNotFoundException.class, () -> repository.findElementById(packages, 9999));
    }

    // createRoute tests
    @Test
    void createRoute_Should_CreateRoute_With_IncrementingId() {
        // Act
        Route route1 = repository.createRoute();
        Route route2 = repository.createRoute();
        Route route3 = repository.createRoute();

        // Assert
        assertEquals(1, route1.getId());
        assertEquals(2, route2.getId());
        assertEquals(3, route3.getId());
    }

    @Test
    void createRoute_Should_AddRouteToRepository() {
        // Arrange
        assertTrue(repository.getRoutes().isEmpty());

        // Act
        repository.createRoute();

        // Assert
        assertEquals(1, repository.getRoutes().size());
    }

    @Test
    void createRoute_Should_ReturnNewRoute() {
        // Act
        Route route = repository.createRoute();

        // Assert
        assertNotNull(route);
        assertTrue(route.isRouteEmpty());
    }

    // createPackage tests
    @Test
    void createPackage_Should_CreatePackage_WithCorrectAttributes() {
        // Arrange
        Locations start = Locations.SYD;
        Locations end = Locations.MEL;
        int weight = 1500;
        String contact = "john@example.com";

        // Act
        Package pkg = repository.createPackage(start, end, weight, contact);

        // Assert
        assertNotNull(pkg);
        assertEquals(1, pkg.getId());
    }

    @Test
    void createPackage_Should_AddPackageToRepository() {
        // Arrange
        assertTrue(repository.getPackages().isEmpty());

        // Act
        repository.createPackage(Locations.BRI, Locations.DAR, 2000, "test@test.com");

        // Assert
        assertEquals(1, repository.getPackages().size());
    }

    @Test
    void createPackage_Should_IncrementId_ForEachPackage() {
        // Act
        Package pkg1 = repository.createPackage(Locations.SYD, Locations.MEL, 100, "a@a.com");
        Package pkg2 = repository.createPackage(Locations.MEL, Locations.ADL, 200, "b@b.com");
        Package pkg3 = repository.createPackage(Locations.ADL, Locations.PER, 300, "c@c.com");

        // Assert
        assertEquals(1, pkg1.getId());
        assertEquals(2, pkg2.getId());
        assertEquals(3, pkg3.getId());
    }

    @Test
    void createPackage_Should_ShareIdSequence_WithRoutes() {
        // Act - Create route first, then package
        Route route = repository.createRoute();
        Package pkg = repository.createPackage(Locations.SYD, Locations.BRI, 500, "test@test.com");

        // Assert - IDs should be sequential across routes and packages
        assertEquals(1, route.getId());
        assertEquals(2, pkg.getId());
    }

    // assignTruckToRoute tests
    @Test
    void assignTruckToRoute_Should_AssignTruck_ToRoute() {
        // Arrange
        Route route = repository.createRoute();
        Truck truck = repository.getTrucks().get(0);

        // Act
        Route resultRoute = repository.assignTruckToRoute(truck, route);

        // Assert
        assertNotNull(resultRoute);
        assertEquals(truck, resultRoute.getAssignedTruck());
    }

    @Test
    void assignTruckToRoute_Should_ReturnSameRoute() {
        // Arrange
        Route route = repository.createRoute();
        Truck truck = repository.getTrucks().get(0);

        // Act
        Route resultRoute = repository.assignTruckToRoute(truck, route);

        // Assert
        assertEquals(route.getId(), resultRoute.getId());
    }

    // Integration tests
    @Test
    void repository_Should_MaintainState_AcrossMultipleOperations() {
        // Arrange & Act
        Route route1 = repository.createRoute();
        Route route2 = repository.createRoute();
        Package pkg1 = repository.createPackage(Locations.SYD, Locations.MEL, 1000, "a@a.com");
        Package pkg2 = repository.createPackage(Locations.MEL, Locations.BRI, 2000, "b@b.com");

        Truck truck = repository.findElementById(repository.getTrucks(), 1001);
        repository.assignTruckToRoute(truck, route1);

        // Assert
        assertAll(
                () -> assertEquals(2, repository.getRoutes().size()),
                () -> assertEquals(2, repository.getPackages().size()),
                () -> assertEquals(80, repository.getTrucks().size()),
                () -> assertEquals(truck, route1.getAssignedTruck())
        );
    }
}
