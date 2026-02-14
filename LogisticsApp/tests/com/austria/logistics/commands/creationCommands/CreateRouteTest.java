package com.austria.logistics.commands.creationCommands;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.NotLoggedInException;
import com.austria.logistics.models.UserImpl;
import com.austria.logistics.models.enums.UserRole;
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
        repository.login(new UserImpl("Test","Test","Test","Test", "test@test.bg", UserRole.EMPLOYEE));
        createRoute = new CreateRoute(repository);
    }

    @Test
    void execute_Should_Throw_Error_When_User_Not_LoggedIn() {
        //Arrange
        repository.logout();
        //Act,Assert
        Assertions.assertThrows(NotLoggedInException.class, () -> createRoute.execute(List.of("Test")));
    }

    @Test
    void execute_Should_Throw_Error_When_User_Not_LoggedIn_AsEmployee() {
        //Arrange
        repository.logout();
        repository.login(new UserImpl("Test","Test","Test","Test", "test@test.bg", UserRole.CUSTOMER));
        //Act,Assert
        Assertions.assertThrows(NotLoggedInException.class, () -> createRoute.execute(List.of("Test")));
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