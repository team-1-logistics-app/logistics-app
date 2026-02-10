package com.austria.logistics.commands.creationCommands;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.UserImpl;
import com.austria.logistics.models.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


class CreatePackageTest {
    private Repository repository;
    Command createPackage;

    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
        repository.login(new UserImpl("Test","Test","Test","Test", UserRole.CUSTOMER));
        createPackage = new CreatePackage(repository);
    }
    @Test
    void execute_Should_Return_Error_When_User_Not_LoggedIn() {
        //Arrange
        repository.logout();
        //Act,Assert
        Assertions.assertEquals("You are not logged in! Please login first!", createPackage.execute(List.of("Sydney", "Darwin", "40")));
    }

    @Test
    void execute_Should_Return_Error_When_ArgumentsCount_isInvalid() {
        //Act,Assert
        Assertions.assertEquals("Invalid number of arguments. Expected: 4, Received: 3.", createPackage.execute(List.of("Sydney", "Darwin", "40")));
    }

    @Test
    void execute_Should_Return_Error_When_Weight_IsLessOrEqual_To_Zero() {
        //Act,Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals("Weight can't be 0 or less kg.", createPackage.execute(List.of("Sydney", "Darwin", "0", "test@test.bg"))),
                () -> Assertions.assertEquals("Weight can't be 0 or less kg.", createPackage.execute(List.of("Sydney", "Darwin", "-1", "test@test.bg")))
        );
    }

    @Test
    void execute_Should_Return_Error_When_Argument_isInvalid() {
        //Act,Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals("Test is not valid location, the supported locations are: Sydney, Melbourne, Adelaide, Alice Springs, Brisbane, Darwin, Perth.",
                        createPackage.execute(List.of("Test", "Darwin", "40", "test@test.com"))),
                () -> Assertions.assertEquals("Test is not valid location, the supported locations are: Sydney, Melbourne, Adelaide, Alice Springs, Brisbane, Darwin, Perth.",
                        createPackage.execute(List.of("Sydney", "Test", "40", "test@test.com"))),
                () -> Assertions.assertEquals("Weight has to be valid integer.",
                        createPackage.execute(List.of("Sydney", "Darwin", "asd", "test@test.com")))
        );
    }

    @Test
    void execute_Should_Return_Info_Message_When_Package_IsCreated() {
        //Act,Assert
        Assertions.assertEquals("Package with id 1 was created!", createPackage.execute(List.of("Sydney", "Darwin", "40", "test@test.com")));
    }

    @Test
    void execute_Should_Create_Package_InRepo() {
        //Act
        createPackage.execute(List.of("Sydney", "Darwin", "40", "test@test.com"));
        // Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, repository.getPackages().size()),
                () -> Assertions.assertEquals(1, repository.getPackages().get(0).getId()),
                () -> Assertions.assertEquals(40, repository.getPackages().get(0).getWeight()),
                () -> Assertions.assertEquals("Sydney", repository.getPackages().get(0).getStartLocation().getDisplayName()),
                () -> Assertions.assertEquals("Darwin", repository.getPackages().get(0).getEndLocation().getDisplayName())
        );
    }
}