package com.austria.logistics.models;

import com.austria.logistics.models.enums.Locations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;


class LocationImplTest {

    @Test
    public void constructor_WithSingeArgument_Should_InitializeLocation_When_ArgumentsAreValid() {
        //Arrange,Act,Assert
        Assertions.assertEquals(Locations.DAR, new LocationImpl(Locations.DAR).getLocation());
    }

    @Test
    public void constructor_WithTwoArguments_Should_InitializeLocation_When_ArgumentsAreValid() {
        //Arrange
        LocalDateTime fixedTime = LocalDateTime.now();
        // Act,Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals(Locations.DAR, new LocationImpl(Locations.DAR, fixedTime).getLocation()),
                () -> Assertions.assertEquals(fixedTime, new LocationImpl(Locations.DAR, fixedTime).getEventTime())
        );
    }

}