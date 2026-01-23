package com.austria.logistics.models.contracts;

import com.austria.logistics.models.enums.Locations;

import java.time.LocalDateTime;
import java.util.LinkedList;

public interface Route extends Identifiable{
    String addLocationToRoute(Locations location, LocalDateTime eventTime);
    String removeLocationFromRoute(Locations location);
    boolean containsLocation(Locations location);
    Location findByCity(Locations location);
    LinkedList<Location> getRoute();
    boolean isRouteEmpty();
    int calculateTotalDistance();
    int calculateDistanceBetween(Locations startLocation, Locations endLocaiton);

}
