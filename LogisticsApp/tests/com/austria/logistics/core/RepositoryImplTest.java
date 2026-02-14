package com.austria.logistics.core;

import com.austria.logistics.exceptions.*;
import com.austria.logistics.models.UserImpl;
import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.CityName;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.models.vehicles.contracts.Truck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


class RepositoryImplTest {
    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 1, 24, 12, 0);

    private Repository repository;

    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
    }

    @Test
    void constructor_Should_Initialize_Trucks() {
        assertEquals(40, repository.getTrucks().size());
    }

    @Test
    void constructor_Should_Initialize_EmptyCollections() {
        Assertions.assertAll(
                () -> assertEquals(0, repository.getRoutes().size()),
                () -> assertEquals(0, repository.getPackages().size()),
                () -> assertEquals(0, repository.getUsers().size())
        );
    }

    @Test
    void createRoute_Should_CreateRoute_And_AddToRepo() {
        Route route = repository.createRoute();

        Assertions.assertAll(
                () -> assertEquals(1, repository.getRoutes().size()),
                () -> assertEquals(1, route.getId()),
                () -> assertTrue(route.getRouteLocations().isEmpty())
        );
    }

    @Test
    void createRoute_Should_IncrementId() {
        Route route1 = repository.createRoute();
        Route route2 = repository.createRoute();

        assertEquals(1, route1.getId());
        assertEquals(2, route2.getId());
    }

    @Test
    void createPackage_Should_CreatePackage_And_AddToRepo() {
        Package pkg = repository.createPackage(CityName.SYD, CityName.DAR, 40, "test@test.com");

        Assertions.assertAll(
                () -> assertEquals(1, repository.getPackages().size()),
                () -> assertEquals(1, pkg.getId()),
                () -> assertEquals(CityName.SYD, pkg.getStartLocation()),
                () -> assertEquals(CityName.DAR, pkg.getEndLocation()),
                () -> assertEquals(40, pkg.getWeight())
        );
    }

    @Test
    void addUser_Should_AddUser_ToRepo() {
        User user = new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.EMPLOYEE);
        repository.addUser(user);

        assertEquals(1, repository.getUsers().size());
    }

    @Test
    void addUser_Should_ThrowException_When_UserAlreadyExists() {
        User user = new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.EMPLOYEE);
        repository.addUser(user);

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> repository.addUser(user));
    }

    @Test
    void login_Should_SetLoggedUser() {
        User user = new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.EMPLOYEE);
        repository.login(user);

        assertEquals(user, repository.getLoggedUser());
    }

    @Test
    void logout_Should_ClearLoggedUser() {
        repository.login(new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.EMPLOYEE));
        repository.logout();

        assertNull(repository.getLoggedUser());
    }

    @Test
    void hasLoggedUser_ShouldReturnTrue_WhenUserIsLoggedIn() {
        repository.login(new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.EMPLOYEE));

        assertTrue(repository.hasLoggedUser());
    }

    @Test
    void hasLoggedUser_ShouldReturnFalse_WhenNoUserLoggedIn() {
        assertFalse(repository.hasLoggedUser());
    }

    @Test
    void findElementById_Should_ReturnElement_When_Found() {
        assertEquals(1001, repository.findElementById(repository.getTrucks(), 1001).getId());
    }

    @Test
    void findElementById_Should_ThrowException_When_NotFound() {
        Assertions.assertThrows(ElementNotFoundException.class,
                () -> repository.findElementById(repository.getTrucks(), 9999));
    }

    @Test
    void createUser_Should_CreateUser() {
        User user = repository.createUser("Test", "Test", "Test", "Test", "test@test.bg", UserRole.EMPLOYEE);

        Assertions.assertAll(
                () -> assertEquals("Test", user.getUsername()),
                () -> assertEquals("test@test.bg", user.getEmail()),
                () -> assertEquals(UserRole.EMPLOYEE, user.getUserRole())
        );
    }

    @Test
    void findUserByUsername_Should_ReturnUser_When_Found() {
        User user = new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.EMPLOYEE);
        repository.addUser(user);

        assertEquals("Test", repository.findUserByUsername("Test").getUsername());
    }

    @Test
    void findUserByUsername_Should_ThrowException_When_NotFound() {
        Assertions.assertThrows(UserNotFoundException.class, () -> repository.findUserByUsername("NonExistent"));
    }

    @Test
    void findUserByEmail_Should_ReturnUser_When_Found() {
        repository.addUser(new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.EMPLOYEE));

        assertEquals("test@test.bg", repository.findUserByEmail("test@test.bg").getEmail());
    }

    @Test
    void findUserByEmail_Should_ThrowException_When_NotFound() {
        Assertions.assertThrows(UserNotFoundException.class, () -> repository.findUserByEmail("noone@test.bg"));
    }

    @Test
    void assignTruckToRoute_Should_AssignTruck_When_RouteHasLocations() {
        Route route = repository.createRoute();
        route.addFirstLocationToRoute(CityName.BRI, FIXED_TIME);
        route.addLocationToRoute(CityName.ADL);
        Truck truck = repository.findElementById(repository.getTrucks(), 1011);

        repository.assignTruckToRoute(truck, route);

        Assertions.assertAll(
                () -> assertTrue(truck.isAssigned()),
                () -> assertEquals(route.getId(), truck.getAssignedRoute().getId()),
                () -> assertTrue(route.hasAssignedTruck())
        );
    }

    @Test
    void assignTruckToRoute_Should_ThrowException_When_Route_HasLessThanTwoLocations() {
        Route route = repository.createRoute();
        Truck truck = repository.findElementById(repository.getTrucks(), 1011);

        Assertions.assertThrows(RouteIsEmptyException.class, () -> repository.assignTruckToRoute(truck, route));
    }

    @Test
    void unassignTruckFromRoute_Should_UnassignTruck() {
        Route route = repository.createRoute();
        route.addFirstLocationToRoute(CityName.BRI, FIXED_TIME);
        route.addLocationToRoute(CityName.ADL);
        Truck truck = repository.findElementById(repository.getTrucks(), 1011);
        repository.assignTruckToRoute(truck, route);

        repository.unassignTruckFromRoute(truck, route);

        assertFalse(truck.isAssigned());
        assertNull(truck.getAssignedRoute());
    }

    @Test
    void unassignTruckFromRoute_Should_ThrowException_When_TruckNotAssigned() {
        Route route = repository.createRoute();
        route.addFirstLocationToRoute(CityName.BRI, FIXED_TIME);
        route.addLocationToRoute(CityName.ADL);
        Truck truck = repository.findElementById(repository.getTrucks(), 1011);

        Assertions.assertThrows(TruckNotAssignedToRouteException.class,
                () -> repository.unassignTruckFromRoute(truck, route));
    }

    @Test
    void assignPackageToTruck_Should_AssignPackage() {
        Route route = repository.createRoute();
        route.addFirstLocationToRoute(CityName.BRI, FIXED_TIME);
        route.addLocationToRoute(CityName.ADL);
        route.addLocationToRoute(CityName.DAR);
        Truck truck = repository.findElementById(repository.getTrucks(), 1011);
        repository.assignTruckToRoute(truck, route);
        Package pkg = repository.createPackage(CityName.BRI, CityName.ADL, 40, "test@test.com");

        repository.assignPackageToTruck(pkg, truck);

        Assertions.assertAll(
                () -> assertTrue(pkg.isAssigned()),
                () -> assertEquals(40, truck.getCurrentWeight()),
                () -> assertTrue(truck.getAssignedPackagesIdList().contains(pkg.getId()))
        );
    }

    @Test
    void assignPackageToTruck_Should_ThrowException_When_TruckNotAssigned() {
        Truck truck = repository.findElementById(repository.getTrucks(), 1011);
        Package pkg = repository.createPackage(CityName.BRI, CityName.ADL, 40, "test@test.com");

        Assertions.assertThrows(TruckNotAssignedToRouteException.class,
                () -> repository.assignPackageToTruck(pkg, truck));
    }

    @Test
    void assignPackageToTruck_Should_ThrowException_When_PackageAlreadyAssigned() {
        Route route = repository.createRoute();
        route.addFirstLocationToRoute(CityName.BRI, FIXED_TIME);
        route.addLocationToRoute(CityName.ADL);
        Truck truck = repository.findElementById(repository.getTrucks(), 1011);
        repository.assignTruckToRoute(truck, route);
        Package pkg = repository.createPackage(CityName.BRI, CityName.ADL, 40, "test@test.com");
        repository.assignPackageToTruck(pkg, truck);

        Assertions.assertThrows(PackageIsAlreadyAssignedException.class,
                () -> repository.assignPackageToTruck(pkg, truck));
    }

    @Test
    void assignPackageToTruck_Should_ThrowException_When_NoPath() {
        Route route = repository.createRoute();
        route.addFirstLocationToRoute(CityName.BRI, FIXED_TIME);
        route.addLocationToRoute(CityName.ADL);
        Truck truck = repository.findElementById(repository.getTrucks(), 1011);
        repository.assignTruckToRoute(truck, route);
        Package pkg = repository.createPackage(CityName.ADL, CityName.BRI, 40, "test@test.com");

        Assertions.assertThrows(NoPathException.class,
                () -> repository.assignPackageToTruck(pkg, truck));
    }

    @Test
    void unassignPackageFromTruck_Should_UnassignPackage() {
        Route route = repository.createRoute();
        route.addFirstLocationToRoute(CityName.BRI, FIXED_TIME);
        route.addLocationToRoute(CityName.ADL);
        route.addLocationToRoute(CityName.DAR);
        Truck truck = repository.findElementById(repository.getTrucks(), 1011);
        repository.assignTruckToRoute(truck, route);
        Package pkg = repository.createPackage(CityName.BRI, CityName.ADL, 40, "test@test.com");
        repository.assignPackageToTruck(pkg, truck);

        repository.unassignPackageFromTruck(pkg, truck);

        Assertions.assertAll(
                () -> assertFalse(pkg.isAssigned()),
                () -> assertEquals(0, truck.getCurrentWeight()),
                () -> assertFalse(truck.getAssignedPackagesIdList().contains(pkg.getId()))
        );
    }
}
