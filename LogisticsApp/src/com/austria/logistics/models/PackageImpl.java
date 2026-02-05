package com.austria.logistics.models;

import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.models.vehicles.contracts.Truck;

public class PackageImpl implements Package {
    private final int id;
    private Locations startLocation;
    private Locations endLocation;
    private int weight;
    private String contactInformation;
    private Truck assignedToTruck;

    public PackageImpl(int id, Locations startLocation, Locations endLocation, int weight, String contactInformation){
        this.id = id;
        this.setStartLocation(startLocation);
        this.setEndLocation(endLocation);
        this.setWeight(weight);
        this.setContactInformation(contactInformation);
    }

    @Override
    public void setAssignedToTruck(Truck assignedToTruck) {this.assignedToTruck = assignedToTruck;}

    @Override
    public Truck getAssignedToTruck() {
        return this.assignedToTruck;
    }

    @Override
    public void setStartLocation(Locations startLocation) {
        this.startLocation = startLocation;
    }

    @Override
    public Locations getStartLocation() {
        return this.startLocation;
    }

    @Override
    public void setEndLocation(Locations endLocation) {
        this.endLocation = endLocation;
    }

    @Override
    public Locations getEndLocation() {
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
    public boolean isAssigned() {
        return this.assignedToTruck != null;
    }

    @Override
    public int getId() {
        return this.id;
    }
}
