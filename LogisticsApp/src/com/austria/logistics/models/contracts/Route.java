package com.austria.logistics.models.contracts;

import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.models.vehicles.contracts.Truck;

import java.time.LocalDateTime;
import java.util.LinkedList;

public interface Route extends Identifiable, Savealbe{
    String addFirstLocationToRoute(Locations location, LocalDateTime eventTime);
    String addLocationToRoute(Locations location);
    void addLocationFromLoad(Location location);
    String removeLocationFromRoute(Locations location);

    boolean containsLocation(Locations location);
    Location findByCity(Locations location);
    LinkedList<Location> getRouteLocations();
    boolean isRouteEmpty();
    int calculateTotalDistance();
    int calculateDistanceBetween(Locations startLocation, Locations endLocation);
    void calculateSchedule();
    Truck getAssignedTruck();

    void setLoadedTruckId(int truckId);
    int getLoadedTruckId();
    void assignTruck(Truck truck);
    void unassignTruck();
    boolean hasAssignedTruck();
}
