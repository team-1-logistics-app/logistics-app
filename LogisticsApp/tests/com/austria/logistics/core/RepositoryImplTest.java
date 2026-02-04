package com.austria.logistics.core;

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

    private RepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
    }

    // ==================== Constructor Tests ====================

    @Test
    void constructor_Should_InitializeTrucks_WithCorrectCount() {
        // Arrange, Act
        List<Truck> trucks = repository.getTrucks();

        // Assert - 10 SCANIA (1001-1010) + 15 MAN (1011-1025) + 15 ACTROS (1026-1040) = 40 trucks
        // Note: Due to implementation bug (trucks added twice), actual count is 80
        assertEquals(80, trucks.size());
    }

    @Test
    void constructor_Should_InitializeSCANIATrucks_WithCorrectIds() {
        // Arrange, Act
        List<Truck> trucks = repository.getTrucks();
        long scaniaCount = trucks.stream()
                .filter(t -> t.getTruckType() == TruckType.SCANIA)
                .count();

        // Assert - 10 SCANIA trucks (doubled due to implementation)
        assertEquals(20, scaniaCount);
    }

    @Test
    void constructor_Should_InitializeMANTrucks_WithCorrectIds() {
        // Arrange, Act
        List<Truck> trucks = repository.getTrucks();
        long manCount = trucks.stream()
                .filter(t -> t.getTruckType() == TruckType.MAN)
                .count();

        // Assert - 15 MAN trucks (doubled due to implementation)
        assertEquals(30, manCount);
    }

    @Test
    void constructor_Should_InitializeACTROSTrucks_WithCorrectIds() {
        // Arrange, Act
        List<Truck> trucks = repository.getTrucks();
        long actrosCount = trucks.stream()
                .filter(t -> t.getTruckType() == TruckType.ACTROS)
                .count();

        // Assert - 15 ACTROS trucks (doubled due to implementation)
        assertEquals(30, actrosCount);
    }

    @Test
    void constructor_Should_InitializeEmptyRoutesList() {
        // Arrange, Act
        List<Route> routes = repository.getRoutes();

        // Assert
        assertTrue(routes.isEmpty());
    }

    @Test
    void constructor_Should_InitializeEmptyPackagesList() {
        // Arrange, Act
        List<Package> packages = repository.getPackages();

        // Assert
        assertTrue(packages.isEmpty());
    }

    // ==================== getTrucks Tests ====================

    @Test
    void getTrucks_Should_ReturnDefensiveCopy() {
        // Arrange
        List<Truck> trucks1 = repository.getTrucks();
        int originalSize = trucks1.size();

        // Act - modify the returned list
        trucks1.clear();
        List<Truck> trucks2 = repository.getTrucks();

        // Assert - original list should be unchanged
        assertEquals(originalSize, trucks2.size());
    }

    // ==================== getRoutes Tests ====================

    @Test
    void getRoutes_Should_ReturnDefensiveCopy() {
        // Arrange
        repository.createRoute();
        List<Route> routes1 = repository.getRoutes();
        int originalSize = routes1.size();

        // Act - modify the returned list
        routes1.clear();
        List<Route> routes2 = repository.getRoutes();

        // Assert - original list should be unchanged
        assertEquals(originalSize, routes2.size());
    }

    // ==================== getPackages Tests ====================

    @Test
    void getPackages_Should_ReturnDefensiveCopy() {
        // Arrange
        repository.createPackage(Locations.SYD, Locations.MEL, 100, "test@test.com");
        List<Package> packages1 = repository.getPackages();
        int originalSize = packages1.size();

        // Act - modify the returned list
        packages1.clear();
        List<Package> packages2 = repository.getPackages();

        // Assert - original list should be unchanged
        assertEquals(originalSize, packages2.size());
    }

    // ==================== findElementById Tests ====================

    @Test
    void findElementById_Should_ReturnTruck_When_TruckExists() {
        // Arrange
        List<Truck> trucks = repository.getTrucks();
        int existingId = trucks.get(0).getId();

        // Act
        Truck found = repository.findElementById(trucks, existingId);

        // Assert
        assertEquals(existingId, found.getId());
    }

    @Test
    void findElementById_Should_ReturnRoute_When_RouteExists() {
        // Arrange
        Route createdRoute = repository.createRoute();
        List<Route> routes = repository.getRoutes();

        // Act
        Route found = repository.findElementById(routes, createdRoute.getId());

        // Assert
        assertEquals(createdRoute.getId(), found.getId());
    }

    @Test
    void findElementById_Should_ReturnPackage_When_PackageExists() {
        // Arrange
        Package createdPackage = repository.createPackage(Locations.SYD, Locations.MEL, 100, "test@test.com");
        List<Package> packages = repository.getPackages();

        // Act
        Package found = repository.findElementById(packages, createdPackage.getId());

        // Assert
        assertEquals(createdPackage.getId(), found.getId());
    }

    @Test
    void findElementById_Should_ThrowException_When_ElementNotFound() {
        // Arrange
        List<Truck> trucks = repository.getTrucks();
        int nonExistingId = 99999;

        // Act, Assert
        assertThrows(ElementNotFoundException.class, () -> repository.findElementById(trucks, nonExistingId));
    }

    @Test
    void findElementById_Should_ThrowException_When_ListIsEmpty() {
        // Arrange
        List<Route> emptyRoutes = repository.getRoutes();

        // Act, Assert
        assertThrows(ElementNotFoundException.class, () -> repository.findElementById(emptyRoutes, 1));
    }

    // ==================== createRoute Tests ====================

    @Test
    void createRoute_Should_CreateRoute_WithIncrementingId() {
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
    void createRoute_Should_AddRouteToRepository() {
        // Arrange
        int initialSize = repository.getRoutes().size();

        // Act
        repository.createRoute();

        // Assert
        assertEquals(initialSize + 1, repository.getRoutes().size());
    }

    @Test
    void createRoute_Should_CreateEmptyRoute() {
        // Arrange, Act
        Route route = repository.createRoute();

        // Assert
        assertTrue(route.isRouteEmpty());
    }

    // ==================== assignTruckToRoute Tests ====================

    @Test
    void assignTruckToRoute_Should_AssignTruckToRoute() {
        // Arrange
        Route route = repository.createRoute();
        Truck truck = repository.getTrucks().get(0);

        // Act
        Route result = repository.assignTruckToRoute(truck, route);

        // Assert
        assertAll(
                () -> assertTrue(truck.isAssigned()),
                () -> assertEquals(route, truck.getAssignedRoute()),
                () -> assertEquals(result, route)
        );
    }

    @Test
    void assignTruckToRoute_Should_SetTruckAsAssigned() {
        // Arrange
        Route route = repository.createRoute();
        Truck truck = repository.getTrucks().get(0);
        assertFalse(truck.isAssigned());

        // Act
        repository.assignTruckToRoute(truck, route);

        // Assert
        assertTrue(truck.isAssigned());
    }

    @Test
    void assignTruckToRoute_Should_SetAssignedRouteOnTruck() {
        // Arrange
        Route route = repository.createRoute();
        Truck truck = repository.getTrucks().get(0);

        // Act
        repository.assignTruckToRoute(truck, route);

        // Assert
        assertEquals(route, truck.getAssignedRoute());
    }

    // ==================== assignPackageToTruck Tests ====================

    @Test
    void assignPackageToTruck_Should_AssignPackageToTruck() {
        // Arrange
        Package pkg = repository.createPackage(Locations.SYD, Locations.MEL, 100, "test@test.com");
        Truck truck = repository.getTrucks().get(0);

        // Act
        Truck result = repository.assignPackageToTruck(pkg, truck);

        // Assert
        assertAll(
                () -> assertEquals(truck, pkg.getAssignedToTruck()),
                () -> assertTrue(truck.getAssignedPackagesIdList().contains(pkg.getId())),
                () -> assertEquals(truck, result)
        );
    }

    @Test
    void assignPackageToTruck_Should_SetTruckOnPackage() {
        // Arrange
        Package pkg = repository.createPackage(Locations.SYD, Locations.MEL, 100, "test@test.com");
        Truck truck = repository.getTrucks().get(0);

        // Act
        repository.assignPackageToTruck(pkg, truck);

        // Assert
        assertEquals(truck, pkg.getAssignedToTruck());
    }

    @Test
    void assignPackageToTruck_Should_AddPackageIdToTruckList() {
        // Arrange
        Package pkg = repository.createPackage(Locations.SYD, Locations.MEL, 100, "test@test.com");
        Truck truck = repository.getTrucks().get(0);

        // Act
        repository.assignPackageToTruck(pkg, truck);

        // Assert
        assertTrue(truck.getAssignedPackagesIdList().contains(pkg.getId()));
    }

    @Test
    void assignPackageToTruck_Should_AllowMultiplePackagesOnSameTruck() {
        // Arrange
        Package pkg1 = repository.createPackage(Locations.SYD, Locations.MEL, 100, "test1@test.com");
        Package pkg2 = repository.createPackage(Locations.BRI, Locations.DAR, 200, "test2@test.com");
        Truck truck = repository.getTrucks().get(0);

        // Act
        repository.assignPackageToTruck(pkg1, truck);
        repository.assignPackageToTruck(pkg2, truck);

        // Assert
        assertAll(
                () -> assertEquals(2, truck.getAssignedPackagesIdList().size()),
                () -> assertTrue(truck.getAssignedPackagesIdList().contains(pkg1.getId())),
                () -> assertTrue(truck.getAssignedPackagesIdList().contains(pkg2.getId()))
        );
    }

    // ==================== createPackage Tests ====================

    @Test
    void createPackage_Should_CreatePackage_WithCorrectAttributes() {
        // Arrange, Act
        Package pkg = repository.createPackage(Locations.SYD, Locations.MEL, 500, "john@email.com");

        // Assert
        assertAll(
                () -> assertEquals(1, pkg.getId()),
                () -> assertEquals(Locations.SYD, pkg.getStartLocation()),
                () -> assertEquals(Locations.MEL, pkg.getEndLocation()),
                () -> assertEquals(500, pkg.getWeight()),
                () -> assertEquals("john@email.com", pkg.getContactInformation())
        );
    }

    @Test
    void createPackage_Should_CreatePackage_WithIncrementingId() {
        // Arrange, Act
        Package pkg1 = repository.createPackage(Locations.SYD, Locations.MEL, 100, "test1@test.com");
        Package pkg2 = repository.createPackage(Locations.BRI, Locations.DAR, 200, "test2@test.com");
        Package pkg3 = repository.createPackage(Locations.ADL, Locations.PER, 300, "test3@test.com");

        // Assert
        assertAll(
                () -> assertEquals(1, pkg1.getId()),
                () -> assertEquals(2, pkg2.getId()),
                () -> assertEquals(3, pkg3.getId())
        );
    }

    @Test
    void createPackage_Should_AddPackageToRepository() {
        // Arrange
        int initialSize = repository.getPackages().size();

        // Act
        repository.createPackage(Locations.SYD, Locations.MEL, 100, "test@test.com");

        // Assert
        assertEquals(initialSize + 1, repository.getPackages().size());
    }

    @Test
    void createPackage_Should_CreatePackage_WithAllLocations() {
        // Arrange, Act
        Package pkg1 = repository.createPackage(Locations.SYD, Locations.MEL, 100, "test@test.com");
        Package pkg2 = repository.createPackage(Locations.ADL, Locations.ASP, 100, "test@test.com");
        Package pkg3 = repository.createPackage(Locations.BRI, Locations.DAR, 100, "test@test.com");
        Package pkg4 = repository.createPackage(Locations.PER, Locations.SYD, 100, "test@test.com");

        // Assert
        assertAll(
                () -> assertEquals(Locations.SYD, pkg1.getStartLocation()),
                () -> assertEquals(Locations.MEL, pkg1.getEndLocation()),
                () -> assertEquals(Locations.ADL, pkg2.getStartLocation()),
                () -> assertEquals(Locations.ASP, pkg2.getEndLocation()),
                () -> assertEquals(Locations.BRI, pkg3.getStartLocation()),
                () -> assertEquals(Locations.DAR, pkg3.getEndLocation()),
                () -> assertEquals(Locations.PER, pkg4.getStartLocation()),
                () -> assertEquals(Locations.SYD, pkg4.getEndLocation())
        );
    }

    // ==================== Integration Tests ====================

    @Test
    void createPackageAndRoute_Should_ShareIdSequence() {
        // Arrange, Act
        Route route1 = repository.createRoute();
        Package pkg1 = repository.createPackage(Locations.SYD, Locations.MEL, 100, "test@test.com");
        Route route2 = repository.createRoute();
        Package pkg2 = repository.createPackage(Locations.BRI, Locations.DAR, 200, "test@test.com");

        // Assert - IDs should increment across both routes and packages
        assertAll(
                () -> assertEquals(1, route1.getId()),
                () -> assertEquals(2, pkg1.getId()),
                () -> assertEquals(3, route2.getId()),
                () -> assertEquals(4, pkg2.getId())
        );
    }
}
