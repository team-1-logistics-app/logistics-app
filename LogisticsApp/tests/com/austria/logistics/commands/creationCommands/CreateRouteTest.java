package com.austria.logistics.commands.creationCommands;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.contracts.Route;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreateRouteTest {

    private Repository repository;
    private CreateRoute createRouteCommand;

    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
        createRouteCommand = new CreateRoute(repository);
    }

    // ==================== Validation Tests ====================

    @Test
    void execute_Should_ReturnError_When_ArgumentsProvided() {
        // Arrange
        List<String> parameters = Arrays.asList("unexpected");

        // Act
        String result = createRouteCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE, 0, 1);
        assertEquals(expectedMessage, result);
    }

    @Test
    void execute_Should_ReturnError_When_MultipleArgumentsProvided() {
        // Arrange
        List<String> parameters = Arrays.asList("arg1", "arg2", "arg3");

        // Act
        String result = createRouteCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE, 0, 3);
        assertEquals(expectedMessage, result);
    }

    // ==================== Success Tests ====================

    @Test
    void execute_Should_CreateRoute_When_NoArgumentsProvided() {
        // Arrange
        List<String> parameters = new ArrayList<>();

        // Act
        String result = createRouteCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.ROUTE_CREATED_MESSAGE, 1);
        assertEquals(expectedMessage, result);
    }

    @Test
    void execute_Should_ReturnCorrectMessage_When_RouteCreated() {
        // Arrange
        List<String> parameters = new ArrayList<>();

        // Act
        String result = createRouteCommand.execute(parameters);

        // Assert
        assertTrue(result.contains("Route with id"));
        assertTrue(result.contains("was created"));
    }

    @Test
    void execute_Should_AddRouteToRepository() {
        // Arrange
        List<String> parameters = new ArrayList<>();
        int initialCount = repository.getRoutes().size();

        // Act
        createRouteCommand.execute(parameters);

        // Assert
        assertEquals(initialCount + 1, repository.getRoutes().size());
    }

    @Test
    void execute_Should_CreateRouteWithIncrementingIds() {
        // Arrange
        List<String> parameters = new ArrayList<>();

        // Act
        String result1 = createRouteCommand.execute(parameters);
        String result2 = createRouteCommand.execute(parameters);
        String result3 = createRouteCommand.execute(parameters);

        // Assert
        assertAll(
                () -> assertEquals(String.format(Constants.ROUTE_CREATED_MESSAGE, 1), result1),
                () -> assertEquals(String.format(Constants.ROUTE_CREATED_MESSAGE, 2), result2),
                () -> assertEquals(String.format(Constants.ROUTE_CREATED_MESSAGE, 3), result3)
        );
    }

    @Test
    void execute_Should_CreateEmptyRoute() {
        // Arrange
        List<String> parameters = new ArrayList<>();

        // Act
        createRouteCommand.execute(parameters);

        // Assert
        Route createdRoute = repository.getRoutes().get(0);
        assertTrue(createdRoute.isRouteEmpty());
    }

    @Test
    void execute_Should_CreateMultipleRoutes_InRepository() {
        // Arrange
        List<String> parameters = new ArrayList<>();

        // Act
        createRouteCommand.execute(parameters);
        createRouteCommand.execute(parameters);
        createRouteCommand.execute(parameters);

        // Assert
        List<Route> routes = repository.getRoutes();
        assertAll(
                () -> assertEquals(3, routes.size()),
                () -> assertEquals(1, routes.get(0).getId()),
                () -> assertEquals(2, routes.get(1).getId()),
                () -> assertEquals(3, routes.get(2).getId())
        );
    }

    @Test
    void execute_Should_CreateRoute_WithCorrectIdFormat() {
        // Arrange
        List<String> parameters = new ArrayList<>();

        // Act
        String result = createRouteCommand.execute(parameters);

        // Assert
        // Verify the message matches expected format: "Route with id X was created!"
        assertTrue(result.matches("Route with id \\d+ was created!"));
    }

    // ==================== Constant Verification Tests ====================

    @Test
    void expectedNumberOfArguments_Should_BeZero() {
        // Assert
        assertEquals(0, CreateRoute.EXPECTED_NUMBER_OF_ARGUMENTS);
    }

    // ==================== Repository State Tests ====================

    @Test
    void execute_Should_NotAffectExistingRoutes() {
        // Arrange
        List<String> parameters = new ArrayList<>();
        createRouteCommand.execute(parameters); // Create first route
        Route firstRoute = repository.getRoutes().get(0);
        int firstRouteId = firstRoute.getId();

        // Act
        createRouteCommand.execute(parameters); // Create second route

        // Assert
        assertEquals(firstRouteId, repository.getRoutes().get(0).getId());
    }

    @Test
    void execute_Should_CreateRoute_WithUniqueId() {
        // Arrange
        List<String> parameters = new ArrayList<>();

        // Act
        createRouteCommand.execute(parameters);
        createRouteCommand.execute(parameters);
        createRouteCommand.execute(parameters);

        // Assert
        List<Route> routes = repository.getRoutes();
        long uniqueIdCount = routes.stream()
                .map(Route::getId)
                .distinct()
                .count();
        assertEquals(routes.size(), uniqueIdCount);
    }
}
