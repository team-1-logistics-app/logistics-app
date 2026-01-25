package com.austria.logistics.models;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.exceptions.InvalidLocationRouteException;
import com.austria.logistics.exceptions.LocationNotFoundException;
import com.austria.logistics.exceptions.RouteIsEmptyException;
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
    public String addLocationToRoute(Locations location, LocalDateTime eventTime) {
        if (!this.isRouteEmpty() && route.getLast().getLocation() == location) {
            throw new InvalidLocationRouteException(String.format(Constants.LOCATION_PREVIOUS_IS_SAME_MESSAGE, this.getId(), location.getCityName()));
        }
        route.add(new LocationImpl(location, eventTime));
        return String.format(Constants.LOCATION_ADDED_MESSAGE, location.getCityName(), this.getId());
    }

    @Override
    public boolean containsLocation(Locations location) {
        for (Location element : this.route) {
            if (element.getLocation() == location) {
                return true;
            }
        }
        return false;
    }

    //THIS CAN THROW EXCEPTION IF LOCATION IS NOT FOUND, HAVE TO DECIDE WHERE TO HANDLE IT
    @Override
    public Location findByCity(Locations location) {
        if (this.containsLocation(location)) {
            for (Location element : this.route) {
                if (element.getLocation() == location) {
                    return element;
                }
            }
        }
        throw new LocationNotFoundException(String.format(Constants.LOCATION_NOT_FOUND_MESSAGE, location.getCityName()));
    }

    //THIS CAN THROW EXCEPTION IF LOCATION IS NOT FOUND, HAVE TO DECIDE WHERE TO HANDLE IT
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
        return this.route.size() == 0;
    }

    //THIS CAN THROW EXCEPTION IF THE ROUTE LIST IS EMPTY, HAVE TO DECIDE WHERE TO HANDLE IT
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

    //THIS CAN THROW EXCEPTION IF CITIES ARE NOT FOUND OR ENDINDEX IS BEFORE STARTINDEX, HAVE TO DECIDE WHERE TO HANDLE IT
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
}
