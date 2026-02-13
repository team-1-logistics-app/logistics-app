package com.austria.logistics.models.contracts;

import com.austria.logistics.models.enums.CityName;
import com.austria.logistics.models.vehicles.contracts.Truck;

import java.time.LocalDateTime;
import java.util.LinkedList;

public interface Route extends Identifiable, Savealbe{
    String addFirstLocationToRoute(CityName location, LocalDateTime eventTime);
    String addLocationToRoute(CityName location);
    void addLocationFromLoad(Location location);
    String removeLocationFromRoute(CityName location);

    boolean containsLocation(CityName location);
    Location findByCity(CityName location);
    LinkedList<Location> getRouteLocations();
    boolean isRouteEmpty();
    int calculateTotalDistance();
    int calculateDistanceBetween(CityName startLocation, CityName endLocation);
    void calculateSchedule();
    Truck getAssignedTruck();

    void setLoadedTruckId(int truckId);
    int getLoadedTruckId();
    void assignTruck(Truck truck);
    void unassignTruck();
    boolean hasAssignedTruck();
}
