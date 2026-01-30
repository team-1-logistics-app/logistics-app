package com.austria.logistics.models;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.exceptions.*;
import com.austria.logistics.models.contracts.Location;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.enums.Locations;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.ListIterator;

public class RouteImpl implements Route {
    private int id;
    private LinkedList<Location> route;

    public RouteImpl(int id) {
        this.id = id;
        this.route = new LinkedList<>();
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String addFirstLocationToRoute(Locations location, LocalDateTime eventTime) {
        if (!this.route.isEmpty()) {
            throw new RouteIsNotEmptyException(String.format(Constants.ROUTE_IS_NOT_EMPTY_MESSAGE, this.getId()));
        }
        this.route.add(new LocationImpl(location, eventTime));
        return String.format(Constants.LOCATION_ADDED_MESSAGE, location.getCityName(), this.getId());
    }

    @Override
    public String addLocationToRoute(Locations location) {
        if (!this.isRouteEmpty() && route.getLast().getLocation() == location) {
            throw new InvalidLocationRouteException(String.format(Constants.LOCATION_PREVIOUS_IS_SAME_MESSAGE, this.getId(), location.getCityName()));
        } else if (this.isRouteEmpty()) {
            throw new RouteIsEmptyException(String.format(Constants.ROUTE_IS_EMPTY_WHILE_ADDING_AS_FIRST_LOCATION_MESSAGE, this.getId()));
        }
        route.add(new LocationImpl(location));
        return String.format(Constants.LOCATION_ADDED_MESSAGE, location.getCityName(), this.getId());
    }


    @Override
    public boolean containsLocation(Locations location) {
        return this.route.stream().anyMatch(locationElement -> locationElement.getLocation() == location);
    }


    @Override
    public Location findByCity(Locations location) {
        return this.route.stream()
                .filter(locationElement -> locationElement.getLocation() == location)
                .findFirst()
                .orElseThrow(() -> new LocationNotFoundException(String.format(Constants.LOCATION_NOT_FOUND_MESSAGE, location.getCityName())));
    }


    @Override
    public String removeLocationFromRoute(Locations location) {
        Location locationToRemove = this.findByCity(location);
        this.route.remove(locationToRemove);
        return String.format(Constants.LOCATION_REMOVED_MESSAGE, location, this.getId());
    }


    @Override
    public LinkedList<Location> getRoute() {
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
    public int calculateDistanceBetween(Locations startLocation, Locations endLocation) {
        int distance = 0;
        Location startPoint = this.findByCity(startLocation);
        Location endPoint = this.findByCity(endLocation);
        int startIndex = this.route.indexOf(startPoint);
        int endIndex = this.route.indexOf(endPoint);
        if (endIndex < startIndex) {
            throw new InvalidLocationRouteException(String.format(Constants.LOCATION_ROUTE_INVALID_MESSAGE, this.getId(), startLocation.getCityName(), endLocation.getCityName()));
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
            long minutes = Math.round((Distance.calculateDistance(prevLocation.getLocation(), currentLocation.getLocation()) / Constants.AVERAGE_SPEED_KMH) * 60);

            prevTime = prevTime.plusMinutes(minutes);
            currentLocation.setEventTime(prevTime);
            prevLocation = currentLocation;
        }
    }

}
