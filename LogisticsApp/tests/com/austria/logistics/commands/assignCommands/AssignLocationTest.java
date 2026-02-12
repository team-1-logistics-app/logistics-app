package com.austria.logistics.commands.assignCommands;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.commands.creationCommands.CreateRoute;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.UserImpl;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.utils.Parsers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;


class AssignLocationTest {
    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 1, 24, 12, 0);

    private Repository repository;
    private Command createRoute;
    private Command assignLocation;
    private Route route;
    private User user;

    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
        user = new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.EMPLOYEE);
        repository.login(user);
        createRoute = new CreateRoute(repository);
        assignLocation = new AssignLocation(repository);
        createRoute.execute(List.of());
        route = repository.getRoutes().get(0);
    }

    @Test
    void executeCommand_Should_Return_Error_When_Not_LoggedIn() {
        //Arrange
        repository.logout();
        //Act,Assert
        Assertions.assertEquals("You are not logged in! Please login first!", assignLocation.execute(List.of("1", "Sydney", "Darwin", Parsers.parseEventTimeToString(FIXED_TIME))));
    }

    @Test
    void executeCommand_Should_Return_Error_When_Not_LoggedIn_As_Employee_Or_Manager() {
        //Arrange
        repository.logout();
        repository.login(new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.CUSTOMER));
        //Act,Assert
        Assertions.assertEquals("You are not logged in as manager or employee!", assignLocation.execute(List.of("1", "Sydney", Parsers.parseEventTimeToString(FIXED_TIME))));
    }

    @Test
    void executeCommand_Should_Return_Error_When_ArgumentCount_isInvalid() {
        //Act,Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals("Invalid number of arguments. Expected: 2, Received: 1.",
                        assignLocation.execute(List.of("1"))),
                () -> Assertions.assertEquals("Invalid number of arguments. Expected: 5, Received: 3.",
                        assignLocation.execute(List.of("1", "Sydney", "Feb"))),
                () -> Assertions.assertEquals("Invalid number of arguments. Expected: 5, Received: 4.",
                        assignLocation.execute(List.of("1", "Sydney", "Feb", "20")))
        );
    }

    @Test
    void executeCommand_Should_Return_Error_When_ArgumentValue_isInvalid() {
        //Act,Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals("Route id has to be valid integer.",
                        assignLocation.execute(List.of("asd", "Sydney", "Feb", "20", "13:00"))),
                () -> Assertions.assertEquals("asd is not valid location, the supported locations are: Sydney, Melbourne, Adelaide, Alice Springs, Brisbane, Darwin, Perth.",
                        assignLocation.execute(List.of("1", "asd", "Feb", "20", "13:00"))),
                () -> Assertions.assertEquals("Invalid event time format. Expected format: MMM d HH:mm.",
                        assignLocation.execute(List.of("1", "Sydney", "asd", "20", "13:00"))),
                () -> Assertions.assertEquals("Invalid event time format. Expected format: MMM d HH:mm.",
                        assignLocation.execute(List.of("1", "Sydney", "Feb", "65", "13:00"))),
                () -> Assertions.assertEquals("Invalid event time format. Expected format: MMM d HH:mm.",
                        assignLocation.execute(List.of("1", "Sydney", "Feb", "20", "65:00")))
        );
    }

    @Test
    void executeCommand_Should_Return_Error_When_RouteId_Is_NotFound() {
        //Act,Assert
        Assertions.assertEquals("No record with id 23 in the repository",
                assignLocation.execute(List.of("23", "Sydney", "Feb", "20", "13:00")));

    }

    @Test
    void executeCommand_Should_Return_Error_When_RouteIsEmpty_And_DepartTime_IsNot_Provided() {
        //Act,Assert
        Assertions.assertEquals("Route with id 1 doesn't contain any locations yet, you should provide departure time for your start location.",
                assignLocation.execute(List.of("1", "Sydney")));
    }

    @Test
    void executeCommand_Should_Assign_FirstLocation_ToRoute() {
        //Act
        assignLocation.execute(List.of("1", "Sydney", "Feb", "20", "13:00"));
        //Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals(1,route.getRouteLocations().size()),
                () -> Assertions.assertEquals("Sydney",route.getRouteLocations().get(0).getLocation().getDisplayName()),
                () -> Assertions.assertEquals("Feb 20 13:00",route.getRouteLocations().get(0).getEventTimeAsString())
        );
    }

    @Test
    void executeCommand_Should_Return_Error_When_Assigning_Second_FirstLocation() {
        //Arrange
        assignLocation.execute(List.of("1", "Sydney", "Feb", "20", "13:00"));
        //Act,Assert
        Assertions.assertEquals("Route with id 1 already contains location with depart time(starting location).",assignLocation.execute(List.of("1", "Sydney", "Feb", "20", "13:00")));
    }

    @Test
    void executeCommand_Should_Assign_Location_ToRoute() {
        //Arrange
        assignLocation.execute(List.of("1", "Sydney", "Feb", "20", "13:00"));
        //Act
        assignLocation.execute(List.of("1", "Darwin"));
        //Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals(2,route.getRouteLocations().size()),
                () -> Assertions.assertEquals("Darwin",route.getRouteLocations().get(1).getLocation().getDisplayName()),
                () -> Assertions.assertEquals("Feb 22 10:14",route.getRouteLocations().get(1).getEventTimeAsString())
        );
    }

    @Test
    void executeCommand_Should_Return_Error_When_LastLocation_IsTheSame() {
        //Arrange
        assignLocation.execute(List.of("1", "Sydney", "Feb", "20", "13:00"));
        assignLocation.execute(List.of("1", "Darwin"));
        //Act, Assert
        Assertions.assertEquals("Route with id 1 already has Darwin as last stop.",assignLocation.execute(List.of("1", "Darwin")));
    }
}