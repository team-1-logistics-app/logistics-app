package com.austria.logistics.models;

import com.austria.logistics.exceptions.*;
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
    public void constructor_Should_InitializeRoute_When_ArgumentsAreValid() {
        //Arrange,Act,Assert
        Assertions.assertEquals(0, new RouteImpl(1).getRouteLocations().size());
    }

    @Test
    void addFirstLocationToRoute_Should_ThrowException_When_Route_isNotEmpty(){
        //Arrange
        route.addFirstLocationToRoute(Locations.BRI,FIXED_TIME);
        //Act, Assert
        Assertions.assertThrows(RouteIsNotEmptyException.class,() ->  route.addFirstLocationToRoute(Locations.DAR,FIXED_TIME));
    }

    @Test
    void addFirstLocationToRoute_When_ArgumentsAreValid(){
        //Arrange
        route.addFirstLocationToRoute(Locations.BRI,FIXED_TIME);
        //Act, Assert
        assertAll(
                () -> assertEquals(1, route.getRouteLocations().size()),
                () -> assertEquals(Locations.BRI, route.getRouteLocations().getFirst().getLocation()),
                () -> assertEquals(FIXED_TIME, route.getRouteLocations().getFirst().getEventTime())
        );
    }

    @Test
    void addLocationToRoute_Should_ThrowException_When_Adding_AsFirst_Location() {
        //Arrange, Act, Assert
        Assertions.assertThrows(RouteIsEmptyException.class,() -> route.addLocationToRoute(Locations.DAR));
    }


   @Test
    void addLocationToRoute_When_ArgumentsAreValid() {
        //Arrange
        route.addFirstLocationToRoute(Locations.DAR,FIXED_TIME);
        route.addLocationToRoute(Locations.BRI);

        //Act,Assert
        assertAll(
                () -> assertEquals(2, route.getRouteLocations().size()),
                () -> assertEquals(Locations.BRI, route.getRouteLocations().get(1).getLocation())
        );
    }

    @Test
    void addLocationToRoute_Should_ThrowException_When_Previous_Location_is_Same() {
        //Arrange
        route.addFirstLocationToRoute(Locations.BRI,FIXED_TIME);

        //Act,Assert
        Assertions.assertThrows(InvalidLocationRouteException.class, () -> route.addLocationToRoute(Locations.BRI));

    }

    @Test
    void addLocationToRoute_Should_Calculate_Schedule_When_LocationIsAdded() {
        //Arrange
        route.addFirstLocationToRoute(Locations.BRI,FIXED_TIME);
        LocalDateTime expectedArrival1 = LocalDateTime.of(2026, 1, 25, 10, 9);
        LocalDateTime expectedArrival2 = LocalDateTime.of(2026, 1, 26, 8, 18);

        //Act
        route.addLocationToRoute(Locations.ADL);
        route.addLocationToRoute(Locations.BRI);

        //Assert
        assertEquals(expectedArrival1, route.getRouteLocations().get(1).getEventTime());
        assertEquals(expectedArrival2, route.getRouteLocations().get(2).getEventTime());
    }

    @Test
    void containsLocation() {
        //Arrange
        route.addFirstLocationToRoute(Locations.ADL,FIXED_TIME);
        route.addLocationToRoute(Locations.BRI);

        //Act,Assert
        assertTrue(route.containsLocation(Locations.BRI));
        assertFalse(route.containsLocation(Locations.DAR));
    }

    @Test
    void findByCity_Should_ReturnEnum_When_LocationExist() {
        //Arrange
        route.addFirstLocationToRoute(Locations.ASP,FIXED_TIME);
        route.addLocationToRoute(Locations.BRI);
        route.addLocationToRoute(Locations.DAR);

        //Act,Assert
        assertEquals(Locations.BRI,route.findByCity(Locations.BRI).getLocation());
        assertEquals(Locations.DAR, route.findByCity(Locations.DAR).getLocation());
    }

    @Test
    void findByCity_Should_ThrowException_When_NotFound() {
        //Arrange
        route.addFirstLocationToRoute(Locations.DAR,FIXED_TIME);
        route.addLocationToRoute(Locations.BRI);

        //Act,Assert
        Assertions.assertThrows(LocationNotFoundException.class,() -> route.findByCity(Locations.SYD).getLocation());
    }

    @Test
    void removeLocationFromRoute_Should_RemoveLocation_FromRoute_When_LocationExist() {
        //Arrange
        route.addFirstLocationToRoute(Locations.ASP,FIXED_TIME);
        route.addLocationToRoute(Locations.BRI);
        route.addLocationToRoute(Locations.DAR);
        route.removeLocationFromRoute(Locations.BRI);

        //Act,Assert
        assertEquals(2,route.getRouteLocations().size());
        assertEquals(Locations.DAR, route.findByCity(Locations.DAR).getLocation());
    }

    @Test
    void removeLocationFromRoute_Should_ThrowException_When_LocationNotFound() {
        //Arrange
        route.addFirstLocationToRoute(Locations.DAR,FIXED_TIME);
        route.addLocationToRoute(Locations.BRI);

        //Act,Assert
        Assertions.assertThrows(LocationNotFoundException.class, () ->  route.removeLocationFromRoute(Locations.SYD));
    }

    @Test
    void getRoute_ShouldReturnEmptyCollection_WhenRouteLocationsIsEmpty() {
        // Act
        LinkedList<Location> result = route.getRouteLocations();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getRoute_Locations_ShouldReturnAllLocations_InCorrectOrder() {
        // Arrange
        route.addFirstLocationToRoute(Locations.DAR,FIXED_TIME);
        route.addLocationToRoute(Locations.BRI);
        route.addLocationToRoute(Locations.SYD);

        // Act
        LinkedList<Location> result = route.getRouteLocations();

        // Assert
        assertAll(
                () -> assertEquals(3, result.size()),
                () -> assertEquals(Locations.DAR, result.get(0).getLocation()),
                () -> assertEquals(Locations.BRI, result.get(1).getLocation()),
                () -> assertEquals(Locations.SYD, result.get(2).getLocation())
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
        route.addFirstLocationToRoute(Locations.DAR,FIXED_TIME);
        route.addLocationToRoute(Locations.BRI);

        // Act
        boolean result = route.isRouteEmpty();

        // Assert
        assertFalse(result);
    }

    @Test
    void calculateTotalDistance_Should_CalculateTotalDistance_When_ThereAreLocations_InTheRoute() {
        // Arrange
        route.addFirstLocationToRoute(Locations.BRI,FIXED_TIME);
        route.addLocationToRoute(Locations.DAR);
        route.addLocationToRoute(Locations.ADL);

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
    void calculateDistanceBetween_Should_CalculateDistance_When_LegalLocationsAre_Passed() {
        //Arrange
        route.addFirstLocationToRoute(Locations.BRI,FIXED_TIME);
        route.addLocationToRoute(Locations.DAR);
        route.addLocationToRoute(Locations.ADL);
        route.addLocationToRoute(Locations.SYD);

        // Act
        int distance1 = route.calculateDistanceBetween(Locations.BRI, Locations.ADL);
        int distance2 = route.calculateDistanceBetween(Locations.DAR, Locations.SYD);

        // Assert
        assertEquals(6453, distance1);
        assertEquals(4403, distance2);
    }

    @Test
    void calculateDistanceBetween_Should_ThrowException_When_Locations_NotFound() {
        // Arrange, Act, Assert
        Assertions.assertThrows(LocationNotFoundException.class, () -> route.calculateDistanceBetween(Locations.BRI,Locations.ADL));
    }

    @Test
    void calculateDistanceBetween_Should_ThrowException_When_Locations_OrderIsInvalid() {
        // Arrange
        route.addFirstLocationToRoute(Locations.BRI,FIXED_TIME);
        route.addLocationToRoute(Locations.DAR);
        route.addLocationToRoute(Locations.ADL);

        //Act, Assert
        Assertions.assertThrows(InvalidLocationRouteException.class, () -> route.calculateDistanceBetween(Locations.ADL,Locations.BRI));
    }
    @Test
    void calculateSchedule_Should_ThrowException_When_Route_isEmpty(){
        //Arrange, Act, Assert
        Assertions.assertThrows(RouteNotEnoughLocationsException.class, () -> route.calculateSchedule());
    }

    @Test
    void calculateSchedule_Should_ThrowException_When_NotEnough_Locations(){
        //Arrange
        route.addFirstLocationToRoute(Locations.BRI,FIXED_TIME);

        //Act, Assert
        Assertions.assertThrows(RouteNotEnoughLocationsException.class, () -> route.calculateSchedule());
    }

    @Test
    void calculateSchedule_Should_Calculate_CorrectTime(){
        //Arrange
        route.addFirstLocationToRoute(Locations.BRI,FIXED_TIME);
        route.addLocationToRoute(Locations.ADL);
        route.addLocationToRoute(Locations.BRI);
        LocalDateTime expectedArrival1 = LocalDateTime.of(2026, 1, 25, 10, 9);
        LocalDateTime expectedArrival2 = LocalDateTime.of(2026, 1, 26, 8, 18);


        route.calculateSchedule();

        //Act, Assert
        assertEquals(expectedArrival1, route.getRouteLocations().get(1).getEventTime());
        assertEquals(expectedArrival2, route.getRouteLocations().get(2).getEventTime());
    }

}