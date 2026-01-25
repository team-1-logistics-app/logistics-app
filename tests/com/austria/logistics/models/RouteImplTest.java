package com.austria.logistics.models;

import com.austria.logistics.exceptions.InvalidLocationRouteException;
import com.austria.logistics.exceptions.LocationNotFoundException;
import com.austria.logistics.exceptions.RouteIsEmptyException;
import com.austria.logistics.models.contracts.Location;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.enums.Locations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;


import static org.junit.jupiter.api.Assertions.*;

class RouteImplTest {

    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026,1,24,12,0);
    private Route route;

    @BeforeEach
    void before() {route = new RouteImpl(1);}


    @Test
    void getId() {
        //Arrange
        Route route1 = new RouteImpl(2);
        Route route2 = new RouteImpl(3);

        //Act,Assert
        assertAll(
                () -> assertEquals(2, route1.getId()),
                () -> assertEquals(3, route2.getId())
        );
    }

   @Test
    void addLocationToRoute() {
        //Arange
        route.addLocationToRoute(Locations.BRI,FIXED_TIME);

        //Act,Assert
        assertAll(
                () -> assertEquals(1, route.getRoute().size()),
                () -> assertEquals(Locations.BRI, route.getRoute().getFirst().getLocation()),
                () -> assertEquals(FIXED_TIME, route.getRoute().getFirst().getEventTime())
        );

    }

    @Test
    void addLocationToRoute_Should_ThrowException_When_Previous_Location_is_Same() {
        //Arange
        route.addLocationToRoute(Locations.BRI,FIXED_TIME);

        //Act,Assert
        Assertions.assertThrows(InvalidLocationRouteException.class, () -> route.addLocationToRoute(Locations.BRI,FIXED_TIME));

    }

    @Test
    void containsLocation() {
        //Arange
        route.addLocationToRoute(Locations.BRI,FIXED_TIME);

        //Act,Assert
        assertEquals(true,route.containsLocation(Locations.BRI));
        assertEquals(false, route.containsLocation(Locations.DAR));
    }

    @Test
    void findByCity() {
        //Arange
        route.addLocationToRoute(Locations.BRI,FIXED_TIME);
        route.addLocationToRoute(Locations.DAR,FIXED_TIME);

        //Act,Assert
        assertEquals(Locations.BRI,route.findByCity(Locations.BRI).getLocation());
        assertEquals(Locations.DAR, route.findByCity(Locations.DAR).getLocation());
    }

    @Test
    void findByCity_Should_ThrowException_When_NotFound() {
        //Arange
        route.addLocationToRoute(Locations.BRI,FIXED_TIME);

        //Act,Assert
        Assertions.assertThrows(LocationNotFoundException.class,() -> route.findByCity(Locations.SYD).getLocation());
    }

    @Test
    void removeLocationFromRoute() {
        //Arange
        route.addLocationToRoute(Locations.BRI,FIXED_TIME);
        route.addLocationToRoute(Locations.DAR,FIXED_TIME);
        route.removeLocationFromRoute(Locations.BRI);

        //Act,Assert
        assertEquals(1,route.getRoute().size());
        assertEquals(Locations.DAR, route.findByCity(Locations.DAR).getLocation());
    }

    @Test
    void removeLocationFromRoute_Should_ThrowException_When_NotFound() {
        //Arange
        route.addLocationToRoute(Locations.BRI,FIXED_TIME);

        //Act,Assert
        Assertions.assertThrows(LocationNotFoundException.class, () ->  route.removeLocationFromRoute(Locations.SYD));
    }

    @Test
    void getRoute_ShouldReturnEmptyCollection_WhenRouteIsEmpty() {
        // Act
        LinkedList<Location> result = route.getRoute();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getRoute_ShouldReturnAllLocations_InCorrectOrder() {
        // Arrange
        route.addLocationToRoute(Locations.BRI, FIXED_TIME);
        route.addLocationToRoute(Locations.SYD, FIXED_TIME);

        // Act
        LinkedList<Location> result = route.getRoute();

        // Assert
        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals(Locations.BRI, result.get(0).getLocation()),
                () -> assertEquals(Locations.SYD, result.get(1).getLocation())
        );
    }


    @Test
    void isRouteEmpty_ShouldReturnTrue_WhenRouteIsEmpty() {
        // Act
        boolean result = route.isRouteEmpty();

        // Assert
        assertTrue(result);
    }

    @Test
    void isRouteEmpty_ShouldReturnFalse_WhenRouteHasLocations() {
        // Arrange
        route.addLocationToRoute(Locations.BRI, FIXED_TIME);

        // Act
        boolean result = route.isRouteEmpty();

        // Assert
        assertFalse(result);
    }

    @Test
    void calculateTotalDistance() {
        // Arrange
        route.addLocationToRoute(Locations.BRI,FIXED_TIME);
        route.addLocationToRoute(Locations.DAR,FIXED_TIME);
        route.addLocationToRoute(Locations.ADL,FIXED_TIME);

        // Act
        int result = route.calculateTotalDistance();

        // Assert
        assertEquals(6453, result);
    }

    @Test
    void calculateTotalDistance_Should_ThrowException_When_Route_IsEmpty() {
        // Arrange, Act, Assert
        Assertions.assertThrows(RouteIsEmptyException.class, () -> route.calculateTotalDistance());
    }

    @Test
    void calculateDistanceBetween() {
        //Arange
        route.addLocationToRoute(Locations.BRI,FIXED_TIME);
        route.addLocationToRoute(Locations.DAR,FIXED_TIME);
        route.addLocationToRoute(Locations.ADL,FIXED_TIME);
        route.addLocationToRoute(Locations.SYD,FIXED_TIME);

        // Act
        int distance1 = route.calculateDistanceBetween(Locations.BRI, Locations.ADL);
        int distance2 = route.calculateDistanceBetween(Locations.DAR, Locations.SYD);

        // Assert
        assertEquals(6453, distance1);
        assertEquals(4403, distance2);
    }

    @Test
    void calculateDistanceBetween_Should_ThrowException_When_Locations_NotFound() {
        // Arange, Act, Assert
        Assertions.assertThrows(LocationNotFoundException.class, () -> route.calculateDistanceBetween(Locations.BRI,Locations.ADL));
    }

    @Test
    void calculateDistanceBetween_Should_ThrowException_When_Locations_OrderIsInvalid() {
        // Arrange
        route.addLocationToRoute(Locations.BRI,FIXED_TIME);
        route.addLocationToRoute(Locations.DAR,FIXED_TIME);
        route.addLocationToRoute(Locations.ADL,FIXED_TIME);

        //Act, Assert
        Assertions.assertThrows(InvalidLocationRouteException.class, () -> route.calculateDistanceBetween(Locations.ADL,Locations.BRI));
    }
}