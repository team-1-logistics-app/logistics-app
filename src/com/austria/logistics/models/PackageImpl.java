package com.austria.logistics.models;

import com.austria.logistics.models.contracts.Location;
import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.enums.Locations;

public class PackageImpl implements Package {
    private final int id;
    private Location startLocation;
    private Location endLocation;
    private int weight;
    private String contactInformation;
    private Route route;

    public PackageImpl(int id, Locations startLocation, Locations endLocation, int weight, String contactInformation){
        this.id = id;
        this.setStartLocation(startLocation);
        this.setEndLocation(endLocation);
        this.setWeight(weight);
        this.setContactInformation(contactInformation);
    }

    @Override
    public void setRoute(Route route) {
        this.route = route;
    }

    @Override
    public Route getRoute() {
        return this.route;
    }

    @Override
    public void setStartLocation(Locations startLocation) {
        this.startLocation = new LocationImpl(startLocation);
    }

    @Override
    public Location getStartLocation() {
        return this.startLocation;
    }

    @Override
    public void setEndLocation(Locations endLocation) {
        this.endLocation = new LocationImpl(endLocation);
    }

    @Override
    public Location getEndLocation() {
        return this.endLocation;
    }

    @Override
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    @Override
    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    @Override
    public String getContactInformation() {
        return this.contactInformation;
    }

    @Override
    public int getId() {
        return this.id;
    }
}
