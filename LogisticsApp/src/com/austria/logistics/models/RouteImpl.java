package com.austria.logistics.models;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.exceptions.*;
import com.austria.logistics.models.contracts.Location;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.enums.CityName;
import com.austria.logistics.models.vehicles.contracts.Truck;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class RouteImpl implements Route {
    private final int id;
    private final LinkedList<Location> route;
    private Truck truck;
    private int loadedTruckId;

    public RouteImpl(int id) {
        this.id = id;
        this.route = new LinkedList<>();
        this.loadedTruckId = -1;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setLoadedTruckId(int truckId) {
        this.loadedTruckId = truckId;
    }

    @Override
    public int getLoadedTruckId() {
        return this.loadedTruckId;
    }

    @Override
    public void assignTruck(Truck truck) {
        this.truck = truck;
    }

    @Override
    public void unassignTruck() { this.truck = null; }

    @Override
    public boolean hasAssignedTruck() {
        return this.truck != null;
    }

    @Override
    public Truck getAssignedTruck() {
        return this.truck;
    }

    @Override
    public String addFirstLocationToRoute(CityName location, LocalDateTime eventTime) {
        if (!this.route.isEmpty()) {
            throw new RouteIsNotEmptyException(String.format(Constants.ROUTE_IS_NOT_EMPTY_MESSAGE, this.getId()));
        }
        this.route.add(new LocationImpl(location, eventTime));
        return String.format(Constants.LOCATION_ADDED_MESSAGE, location.getDisplayName(), this.getId());
    }

    @Override
    public String addLocationToRoute(CityName location) {
        if (!this.isRouteEmpty() && route.getLast().getLocation() == location) {
            throw new InvalidLocationRouteException(String.format(Constants.LOCATION_PREVIOUS_IS_SAME_MESSAGE, this.getId(), location.getDisplayName()));
        } else if (this.isRouteEmpty()) {
            throw new RouteIsEmptyException(String.format(Constants.ROUTE_IS_EMPTY_WHILE_ADDING_AS_FIRST_LOCATION_MESSAGE, this.getId()));
        }
        route.add(new LocationImpl(location));
        this.calculateSchedule();

        return String.format(Constants.LOCATION_ADDED_MESSAGE, location.getDisplayName(), this.getId());
    }

    @Override
    public void addLocationFromLoad(Location location) {
        this.route.add(location);
    }


    @Override
    public boolean containsLocation(CityName location) {
        return this.route.stream().anyMatch(locationElement -> locationElement.getLocation() == location);
    }


    @Override
    public Location findByCity(CityName location) {
        return this.route.stream()
                .filter(locationElement -> locationElement.getLocation() == location)
                .findFirst()
                .orElseThrow(() -> new LocationNotFoundException(String.format(Constants.LOCATION_NOT_FOUND_MESSAGE, location.getDisplayName())));
    }


    @Override
    public String removeLocationFromRoute(CityName location) {
        Location locationToRemove = this.findByCity(location);
        this.route.remove(locationToRemove);
        return String.format(Constants.LOCATION_REMOVED_MESSAGE, location, this.getId());
    }


    @Override
    public LinkedList<Location> getRouteLocations() {
        return new LinkedList<>(this.route);
    }

    @Override
    public boolean isRouteEmpty() {
        return this.route.isEmpty();
    }


    @Override
    public int calculateTotalDistance() {
        if (!this.isRouteEmpty()) {
            int totalDistance = 0;
            ListIterator<Location> iterator = route.listIterator();

            Location previous = iterator.next();
            while (iterator.hasNext()) {
                Location current = iterator.next();
                totalDistance += Distance.calculateDistance(previous.getLocation(), current.getLocation());
                previous = current;
            }
            return totalDistance;
        }
        throw new RouteIsEmptyException(String.format(Constants.ROUTE_IS_EMPTY_MESSAGE, this.getId()));
    }


    @Override
    public int calculateDistanceBetween(CityName startLocation, CityName endLocation) {
        int distance = 0;
        Location startPoint = this.findByCity(startLocation);
        Location endPoint = this.findByCity(endLocation);
        int startIndex = this.route.indexOf(startPoint);
        int endIndex = this.route.indexOf(endPoint);
        if (endIndex < startIndex) {
            throw new InvalidLocationRouteException(String.format(Constants.LOCATION_ROUTE_INVALID_MESSAGE, this.getId(), startLocation.getDisplayName(), endLocation.getDisplayName()));
        }

        for (int i = startIndex; i < endIndex; i++) {
            distance += Distance.calculateDistance(this.route.get(i).getLocation(), this.route.get(i + 1).getLocation());
        }
        return distance;
    }

    @Override
    public void calculateSchedule() {
        if (this.route.size() < 2) {
            throw new RouteNotEnoughLocationsException(String.format(Constants.ROUTE_NOT_ENOUGH_LOCATIONS_MESSAGE, this.getId()));
        }

        LocalDateTime prevTime = this.route.getFirst().getEventTime();
        Location prevLocation = this.route.getFirst();

        for (int i = 1; i < this.route.size(); i++) {

            Location currentLocation = this.route.get(i);
            long minutes = Math.round((Distance.calculateDistance(prevLocation.getLocation(), currentLocation.getLocation()) / Constants.TRUCK_AVERAGE_SPEED_KMH) * 60);

            prevTime = prevTime.plusMinutes(minutes);
            currentLocation.setEventTime(prevTime);
            prevLocation = currentLocation;
        }
    }

    @Override
    public String toString(){
            StringBuilder output = new StringBuilder();
            output.append(String.format("Current schedule for route with id %d:\n", this.getId()));
            if (this.hasAssignedTruck()) {
                Truck assignedTruck = this.getAssignedTruck();
                output.append(String.format("The route has assigned truck %s with id %d.\n", assignedTruck.getTruckType().getDisplayName(), assignedTruck.getId()));
            } else {
                output.append("No assigned truck to the route.\n");
            }
            this.getRouteLocations().forEach(location -> {
                output.append(String.format("City: %s,", location.getLocation().getDisplayName()))
                        .append(String.format(" Scheduled time: %s\n", location.getEventTimeAsString()));
            });
            if (this.getRouteLocations().isEmpty()) {
                output.append("No locations added to the route yet.\n");
            }
            return output.toString();
    }

    @Override
    public String toSaveString() {
        return String.join("|",
                String.valueOf(id),
                route.isEmpty() ? "NONE" : route.stream().map(location -> location.getLocation().getDisplayName() + "@" + location.getEventTimeAsString())
                        .collect(Collectors.joining(",")),
                truck == null ? "NONE" : String.valueOf(truck.getId())
        );
    }
}
