package com.austria.logistics.commands.creationCommands;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.enums.Locations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreatePackageTest {

    private static final int EXPECTED_ARGUMENTS = 4;
    private Repository repository;
    private Command createPackageCommand;

    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
        createPackageCommand = new CreatePackage(repository);
    }

    // Constructor tests
    @Test
    void constructor_Should_InitializeCommand_WithRepository() {
        // Arrange, Act
        CreatePackage command = new CreatePackage(repository);

        // Assert
        assertNotNull(command);
    }

    // Execute tests - Valid scenarios
    @Test
    void execute_Should_CreatePackage_When_AllParametersAreValid() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "1000", "john@example.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertTrue(result.contains("Package with id"));
        assertTrue(result.contains("was created!"));
        assertEquals(1, repository.getPackages().size());
    }

    @Test
    void execute_Should_CreatePackage_With_DifferentLocations() {
        // Arrange
        List<String> parameters = Arrays.asList("Brisbane", "Darwin", "5000", "test@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertEquals(String.format(Constants.PACKAGE_CREATED, 1), result);
    }

    @Test
    void execute_Should_CreatePackage_With_AliceSprings() {
        // Arrange
        List<String> parameters = Arrays.asList("Alice Springs", "Perth", "2500", "contact@company.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertEquals(String.format(Constants.PACKAGE_CREATED, 1), result);
    }

    @Test
    void execute_Should_IncrementPackageId_ForMultiplePackages() {
        // Arrange
        List<String> params1 = Arrays.asList("Sydney", "Melbourne", "100", "a@a.com");
        List<String> params2 = Arrays.asList("Melbourne", "Adelaide", "200", "b@b.com");
        List<String> params3 = Arrays.asList("Adelaide", "Perth", "300", "c@c.com");

        // Act
        String result1 = createPackageCommand.execute(params1);
        String result2 = createPackageCommand.execute(params2);
        String result3 = createPackageCommand.execute(params3);

        // Assert
        assertEquals(String.format(Constants.PACKAGE_CREATED, 1), result1);
        assertEquals(String.format(Constants.PACKAGE_CREATED, 2), result2);
        assertEquals(String.format(Constants.PACKAGE_CREATED, 3), result3);
        assertEquals(3, repository.getPackages().size());
    }

    // Execute tests - Invalid argument count
    @Test
    void execute_Should_ReturnErrorMessage_When_TooFewArguments() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "1000");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE, EXPECTED_ARGUMENTS, 3);
        assertEquals(expectedMessage, result);
        assertTrue(repository.getPackages().isEmpty());
    }

    @Test
    void execute_Should_ReturnErrorMessage_When_TooManyArguments() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "1000", "email@test.com", "extra");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE, EXPECTED_ARGUMENTS, 5);
        assertEquals(expectedMessage, result);
        assertTrue(repository.getPackages().isEmpty());
    }

    @Test
    void execute_Should_ReturnErrorMessage_When_NoArguments() {
        // Arrange
        List<String> parameters = Collections.emptyList();

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE, EXPECTED_ARGUMENTS, 0);
        assertEquals(expectedMessage, result);
    }

    // Execute tests - Invalid locations
    @Test
    void execute_Should_ReturnErrorMessage_When_StartLocationIsInvalid() {
        // Arrange
        List<String> parameters = Arrays.asList("InvalidCity", "Melbourne", "1000", "email@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertTrue(result.contains("InvalidCity"));
        assertTrue(result.contains("is not valid location"));
        assertTrue(repository.getPackages().isEmpty());
    }

    @Test
    void execute_Should_ReturnErrorMessage_When_EndLocationIsInvalid() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "InvalidDestination", "1000", "email@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertTrue(result.contains("InvalidDestination"));
        assertTrue(result.contains("is not valid location"));
        assertTrue(repository.getPackages().isEmpty());
    }

    @Test
    void execute_Should_ReturnErrorMessage_When_BothLocationsAreInvalid() {
        // Arrange
        List<String> parameters = Arrays.asList("City1", "City2", "1000", "email@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert - First invalid location should trigger error
        assertTrue(result.contains("is not valid location"));
        assertTrue(repository.getPackages().isEmpty());
    }

    // Execute tests - Invalid weight
    @Test
    void execute_Should_ReturnErrorMessage_When_WeightIsZero() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "0", "email@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertEquals(Constants.WEIGHT_VALUE_INVALID_MESSAGE, result);
        assertTrue(repository.getPackages().isEmpty());
    }

    @Test
    void execute_Should_ReturnErrorMessage_When_WeightIsNegative() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "-100", "email@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertEquals(Constants.WEIGHT_VALUE_INVALID_MESSAGE, result);
        assertTrue(repository.getPackages().isEmpty());
    }

    @Test
    void execute_Should_ReturnErrorMessage_When_WeightIsNotANumber() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "abc", "email@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.VALUE_INVALID_FORMAT_MESSAGE, "Weight");
        assertEquals(expectedMessage, result);
        assertTrue(repository.getPackages().isEmpty());
    }

    @Test
    void execute_Should_ReturnErrorMessage_When_WeightIsDecimal() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "100.5", "email@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.VALUE_INVALID_FORMAT_MESSAGE, "Weight");
        assertEquals(expectedMessage, result);
        assertTrue(repository.getPackages().isEmpty());
    }

    // Execute tests - Edge cases
    @Test
    void execute_Should_CreatePackage_When_WeightIsOne() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "1", "email@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertEquals(String.format(Constants.PACKAGE_CREATED, 1), result);
        assertEquals(1, repository.getPackages().size());
    }

    @Test
    void execute_Should_CreatePackage_When_WeightIsLarge() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "999999", "email@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertEquals(String.format(Constants.PACKAGE_CREATED, 1), result);
    }

    @Test
    void execute_Should_AcceptAnyContactInfo() {
        // Arrange - Contact info is not validated, any string should work
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "1000", "any contact info here!");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertEquals(String.format(Constants.PACKAGE_CREATED, 1), result);
    }

    // Location case sensitivity tests
    @Test
    void execute_Should_ReturnErrorMessage_When_LocationHasWrongCase() {
        // Arrange - Parsers.parseLocation is case-sensitive
        List<String> parameters = Arrays.asList("sydney", "melbourne", "1000", "email@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertTrue(result.contains("is not valid location"));
    }

    @Test
    void execute_Should_ReturnErrorMessage_When_LocationHasUpperCase() {
        // Arrange
        List<String> parameters = Arrays.asList("SYDNEY", "MELBOURNE", "1000", "email@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertTrue(result.contains("is not valid location"));
    }

    // createPackage method tests
    @Test
    void createPackage_Should_ReturnSuccessMessage_WithCorrectId() {
        // Arrange
        CreatePackage command = new CreatePackage(repository);

        // Act
        String result = command.createPackage(Locations.SYD, Locations.MEL, 500, "test@test.com");

        // Assert
        assertEquals(String.format(Constants.PACKAGE_CREATED, 1), result);
    }

    @Test
    void createPackage_Should_AddPackageToRepository() {
        // Arrange
        CreatePackage command = new CreatePackage(repository);
        assertTrue(repository.getPackages().isEmpty());

        // Act
        command.createPackage(Locations.BRI, Locations.DAR, 1000, "info@company.com");

        // Assert
        assertEquals(1, repository.getPackages().size());
    }
}
