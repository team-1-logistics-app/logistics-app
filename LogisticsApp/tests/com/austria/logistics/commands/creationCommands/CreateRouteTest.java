package com.austria.logistics.commands.creationCommands;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


class CreateRouteTest {
    Repository repository;
    Command createRoute;
    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
        createRoute = new CreateRoute(repository);
    }

    @Test
    void execute_Should_Return_Error_When_ArgumentCount_isInvalid() {
        //Act,Assert
        Assertions.assertEquals("Invalid number of arguments. Expected: 0, Received: 1.", createRoute.execute(List.of("Test")));
    }

    @Test
    void execute_Should_Return_Info_Message_When_Route_IsCreated() {
        //Act,Assert
        Assertions.assertEquals("Route with id 1 was created!", createRoute.execute(List.of()));
    }

    @Test
    void execute_Should_Create_Route_InRepo() {
        //Act
        createRoute.execute(List.of());
        // Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, repository.getRoutes().size()),
                () -> Assertions.assertEquals(1, repository.getRoutes().get(0).getId()),
                () -> Assertions.assertEquals(0, repository.getRoutes().get(0).getRouteLocations().size()),
                () -> Assertions.assertFalse(repository.getRoutes().get(0).hasAssignedTruck())
        );

    }

}