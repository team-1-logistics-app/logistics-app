package com.austria.logistics.commands.creation;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.constants.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreatePackageTest {

    private Command createPackageCommand;

    @BeforeEach
    void before() {
        createPackageCommand = new CreatePackage();
    }

    @Test
    public void execute_Should_ReturnSuccessMessage_When_ArgumentsAreValid() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "45", "John Doe");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertTrue(result.contains("was created"));
    }

    @Test
    public void execute_Should_ReturnErrorMessage_When_TooFewArguments() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "45");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE, 4, 3);
        assertEquals(expectedMessage, result);
    }

    @Test
    public void execute_Should_ReturnErrorMessage_When_TooManyArguments() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "45", "John Doe", "Extra");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE, 4, 5);
        assertEquals(expectedMessage, result);
    }

    @Test
    public void execute_Should_ReturnErrorMessage_When_StartLocationIsInvalid() {
        // Arrange
        List<String> parameters = Arrays.asList("InvalidCity", "Melbourne", "45", "John Doe");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.LOCATION_INVALID_MESSAGE, "InvalidCity");
        assertEquals(expectedMessage, result);
    }

    @Test
    public void execute_Should_ReturnErrorMessage_When_EndLocationIsInvalid() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "InvalidCity", "45", "John Doe");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.LOCATION_INVALID_MESSAGE, "InvalidCity");
        assertEquals(expectedMessage, result);
    }

    @Test
    public void execute_Should_ReturnErrorMessage_When_WeightIsNotNumeric() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "notANumber", "John Doe");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertEquals(Constants.WEIGHT_VALUE_INVALID_FORMAT_MESSAGE, result);
    }

    @Test
    public void execute_Should_ReturnErrorMessage_When_WeightIsZero() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "0", "John Doe");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertEquals(Constants.WEIGHT_VALUE_INVALID_MESSAGE, result);
    }

    @Test
    public void execute_Should_ReturnErrorMessage_When_WeightIsNegative() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "-10", "John Doe");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertEquals(Constants.WEIGHT_VALUE_INVALID_MESSAGE, result);
    }

    @Test
    public void execute_Should_AcceptAllValidLocations() {
        // Arrange
        String[] validLocations = {"Sydney", "Melbourne", "Adelaide", "Alice Springs", "Brisbane", "Darwin", "Perth"};

        for (String startLocation : validLocations) {
            for (String endLocation : validLocations) {
                if (!startLocation.equals(endLocation)) {
                    List<String> parameters = Arrays.asList(startLocation, endLocation, "50", "Contact");
                    
                    // Act
                    String result = new CreatePackage().execute(parameters);

                    // Assert
                    assertTrue(result.contains("was created"),
                            "Failed for route from " + startLocation + " to " + endLocation);
                }
            }
        }
    }

    @Test
    public void execute_Should_AcceptValidWeight() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "42000", "John Doe");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertTrue(result.contains("was created"));
    }

    @Test
    public void execute_Should_AcceptMinimumValidWeight() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "1", "John Doe");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertTrue(result.contains("was created"));
    }

    @Test
    public void execute_Should_ReturnErrorMessage_When_EmptyParametersList() {
        // Arrange
        List<String> parameters = new ArrayList<>();

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE, 4, 0);
        assertEquals(expectedMessage, result);
    }

    @Test
    public void execute_Should_AcceptContactInfoWithSpaces() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "45", "John Doe Smith Jr.");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertTrue(result.contains("was created"));
    }

    @Test
    public void execute_Should_AcceptContactInfoWithSpecialCharacters() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "45", "john.doe@email.com, +61-123-456-789");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertTrue(result.contains("was created"));
    }

    @Test
    public void createPackage_Should_ReturnFormattedMessage_WithPackageId() {
        // Arrange
        CreatePackage command = new CreatePackage();
        List<String> parameters = Arrays.asList("Brisbane", "Perth", "100", "Customer Info");

        // Act
        String result = command.execute(parameters);

        // Assert
        assertTrue(result.matches("Package with id \\d+ was created!"));
    }
}
