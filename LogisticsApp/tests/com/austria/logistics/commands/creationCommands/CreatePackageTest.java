package com.austria.logistics.commands.creationCommands;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.models.enums.Locations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreatePackageTest {

    private Repository repository;
    private CreatePackage createPackageCommand;

    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
        createPackageCommand = new CreatePackage(repository);
    }

    // ==================== Argument Count Validation Tests ====================

    @Test
    void execute_Should_ReturnError_When_NoArgumentsProvided() {
        // Arrange
        List<String> parameters = new ArrayList<>();

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE, 4, 0);
        assertEquals(expectedMessage, result);
    }

    @Test
    void execute_Should_ReturnError_When_TooFewArgumentsProvided() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "100");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE, 4, 3);
        assertEquals(expectedMessage, result);
    }

    @Test
    void execute_Should_ReturnError_When_TooManyArgumentsProvided() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "100", "test@test.com", "extra");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE, 4, 5);
        assertEquals(expectedMessage, result);
    }

    // ==================== Location Validation Tests ====================

    @Test
    void execute_Should_ReturnError_When_StartLocationIsInvalid() {
        // Arrange
        List<String> parameters = Arrays.asList("InvalidCity", "Melbourne", "100", "test@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.LOCATION_INVALID_MESSAGE, "InvalidCity");
        assertEquals(expectedMessage, result);
    }

    @Test
    void execute_Should_ReturnError_When_EndLocationIsInvalid() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "InvalidCity", "100", "test@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.LOCATION_INVALID_MESSAGE, "InvalidCity");
        assertEquals(expectedMessage, result);
    }

    @Test
    void execute_Should_ReturnError_When_LocationIsCaseSensitive() {
        // Arrange
        List<String> parameters = Arrays.asList("sydney", "Melbourne", "100", "test@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert - lowercase "sydney" should be invalid
        String expectedMessage = String.format(Constants.LOCATION_INVALID_MESSAGE, "sydney");
        assertEquals(expectedMessage, result);
    }

    // ==================== Weight Validation Tests ====================

    @Test
    void execute_Should_ReturnError_When_WeightIsNotInteger() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "invalid", "test@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.VALUE_INVALID_FORMAT_MESSAGE, "Weight");
        assertEquals(expectedMessage, result);
    }

    @Test
    void execute_Should_ReturnError_When_WeightIsZero() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "0", "test@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertEquals(Constants.WEIGHT_VALUE_INVALID_MESSAGE, result);
    }

    @Test
    void execute_Should_ReturnError_When_WeightIsNegative() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "-100", "test@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertEquals(Constants.WEIGHT_VALUE_INVALID_MESSAGE, result);
    }

    @Test
    void execute_Should_ReturnError_When_WeightIsDecimal() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "100.5", "test@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.VALUE_INVALID_FORMAT_MESSAGE, "Weight");
        assertEquals(expectedMessage, result);
    }

    // ==================== Success Tests ====================

    @Test
    void execute_Should_CreatePackage_When_ArgumentsAreValid() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "100", "test@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        String expectedMessage = String.format(Constants.PACKAGE_CREATED_MESSAGE, 1);
        assertEquals(expectedMessage, result);
    }

    @Test
    void execute_Should_ReturnCorrectMessage_When_PackageCreated() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "100", "test@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertTrue(result.contains("Package with id"));
        assertTrue(result.contains("was created"));
    }

    @Test
    void execute_Should_AddPackageToRepository() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "100", "test@test.com");
        int initialCount = repository.getPackages().size();

        // Act
        createPackageCommand.execute(parameters);

        // Assert
        assertEquals(initialCount + 1, repository.getPackages().size());
    }

    @Test
    void execute_Should_CreatePackage_WithCorrectAttributes() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "500", "john@email.com");

        // Act
        createPackageCommand.execute(parameters);

        // Assert
        Package createdPackage = repository.getPackages().get(0);
        assertAll(
                () -> assertEquals(Locations.SYD, createdPackage.getStartLocation()),
                () -> assertEquals(Locations.MEL, createdPackage.getEndLocation()),
                () -> assertEquals(500, createdPackage.getWeight()),
                () -> assertEquals("john@email.com", createdPackage.getContactInformation())
        );
    }

    @Test
    void execute_Should_CreatePackage_WithIncrementingIds() {
        // Arrange
        List<String> params1 = Arrays.asList("Sydney", "Melbourne", "100", "test1@test.com");
        List<String> params2 = Arrays.asList("Brisbane", "Darwin", "200", "test2@test.com");
        List<String> params3 = Arrays.asList("Adelaide", "Perth", "300", "test3@test.com");

        // Act
        String result1 = createPackageCommand.execute(params1);
        String result2 = createPackageCommand.execute(params2);
        String result3 = createPackageCommand.execute(params3);

        // Assert
        assertAll(
                () -> assertEquals(String.format(Constants.PACKAGE_CREATED_MESSAGE, 1), result1),
                () -> assertEquals(String.format(Constants.PACKAGE_CREATED_MESSAGE, 2), result2),
                () -> assertEquals(String.format(Constants.PACKAGE_CREATED_MESSAGE, 3), result3)
        );
    }

    // ==================== All Locations Tests ====================

    @Test
    void execute_Should_CreatePackage_WithSydneyLocation() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "100", "test@test.com");

        // Act
        createPackageCommand.execute(parameters);

        // Assert
        assertEquals(Locations.SYD, repository.getPackages().get(0).getStartLocation());
    }

    @Test
    void execute_Should_CreatePackage_WithMelbourneLocation() {
        // Arrange
        List<String> parameters = Arrays.asList("Melbourne", "Sydney", "100", "test@test.com");

        // Act
        createPackageCommand.execute(parameters);

        // Assert
        assertEquals(Locations.MEL, repository.getPackages().get(0).getStartLocation());
    }

    @Test
    void execute_Should_CreatePackage_WithAdelaideLocation() {
        // Arrange
        List<String> parameters = Arrays.asList("Adelaide", "Sydney", "100", "test@test.com");

        // Act
        createPackageCommand.execute(parameters);

        // Assert
        assertEquals(Locations.ADL, repository.getPackages().get(0).getStartLocation());
    }

    @Test
    void execute_Should_CreatePackage_WithAliceSpringsLocation() {
        // Arrange
        List<String> parameters = Arrays.asList("Alice Springs", "Sydney", "100", "test@test.com");

        // Act
        createPackageCommand.execute(parameters);

        // Assert
        assertEquals(Locations.ASP, repository.getPackages().get(0).getStartLocation());
    }

    @Test
    void execute_Should_CreatePackage_WithBrisbaneLocation() {
        // Arrange
        List<String> parameters = Arrays.asList("Brisbane", "Sydney", "100", "test@test.com");

        // Act
        createPackageCommand.execute(parameters);

        // Assert
        assertEquals(Locations.BRI, repository.getPackages().get(0).getStartLocation());
    }

    @Test
    void execute_Should_CreatePackage_WithDarwinLocation() {
        // Arrange
        List<String> parameters = Arrays.asList("Darwin", "Sydney", "100", "test@test.com");

        // Act
        createPackageCommand.execute(parameters);

        // Assert
        assertEquals(Locations.DAR, repository.getPackages().get(0).getStartLocation());
    }

    @Test
    void execute_Should_CreatePackage_WithPerthLocation() {
        // Arrange
        List<String> parameters = Arrays.asList("Perth", "Sydney", "100", "test@test.com");

        // Act
        createPackageCommand.execute(parameters);

        // Assert
        assertEquals(Locations.PER, repository.getPackages().get(0).getStartLocation());
    }

    // ==================== Edge Cases Tests ====================

    @Test
    void execute_Should_CreatePackage_WithSameStartAndEndLocation() {
        // Arrange - Same location for start and end (may be valid depending on business rules)
        List<String> parameters = Arrays.asList("Sydney", "Sydney", "100", "test@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert - Package should be created (no validation for same start/end)
        assertTrue(result.contains("was created"));
    }

    @Test
    void execute_Should_CreatePackage_WithMinimumWeight() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "1", "test@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertTrue(result.contains("was created"));
        assertEquals(1, repository.getPackages().get(0).getWeight());
    }

    @Test
    void execute_Should_CreatePackage_WithLargeWeight() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "100000", "test@test.com");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertTrue(result.contains("was created"));
        assertEquals(100000, repository.getPackages().get(0).getWeight());
    }

    @Test
    void execute_Should_CreatePackage_WithEmptyContactInfo() {
        // Arrange
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "100", "");

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertTrue(result.contains("was created"));
        assertEquals("", repository.getPackages().get(0).getContactInformation());
    }

    @Test
    void execute_Should_CreatePackage_WithSpecialCharactersInContactInfo() {
        // Arrange
        String contactInfo = "john.doe+test@company-name.com.au";
        List<String> parameters = Arrays.asList("Sydney", "Melbourne", "100", contactInfo);

        // Act
        String result = createPackageCommand.execute(parameters);

        // Assert
        assertTrue(result.contains("was created"));
        assertEquals(contactInfo, repository.getPackages().get(0).getContactInformation());
    }

    // ==================== Repository State Tests ====================

    @Test
    void execute_Should_NotAffectExistingPackages() {
        // Arrange
        List<String> params1 = Arrays.asList("Sydney", "Melbourne", "100", "first@test.com");
        createPackageCommand.execute(params1);
        Package firstPackage = repository.getPackages().get(0);
        String firstContactInfo = firstPackage.getContactInformation();

        // Act
        List<String> params2 = Arrays.asList("Brisbane", "Darwin", "200", "second@test.com");
        createPackageCommand.execute(params2);

        // Assert
        assertEquals(firstContactInfo, repository.getPackages().get(0).getContactInformation());
    }

    @Test
    void execute_Should_CreatePackage_WithUniqueId() {
        // Arrange
        List<String> params = Arrays.asList("Sydney", "Melbourne", "100", "test@test.com");

        // Act
        createPackageCommand.execute(params);
        createPackageCommand.execute(params);
        createPackageCommand.execute(params);

        // Assert
        List<Package> packages = repository.getPackages();
        long uniqueIdCount = packages.stream()
                .map(Package::getId)
                .distinct()
                .count();
        assertEquals(packages.size(), uniqueIdCount);
    }

    // ==================== Constant Verification Tests ====================

    @Test
    void expectedNumberOfArguments_Should_BeFour() {
        // Assert
        assertEquals(4, CreatePackage.EXPECTED_NUMBER_OF_ARGUMENTS);
    }
}
