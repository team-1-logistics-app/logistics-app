package com.austria.logistics.models;

import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.models.vehicles.contracts.Truck;
import com.austria.logistics.utils.Parsers;

import java.time.LocalDateTime;

public class PackageImpl implements Package {
    private final int id;
    private Locations startLocation;
    private Locations endLocation;
    private int weight;
    private String contactInformation;
    private Truck assignedTruck;
    private LocalDateTime estimatedArrivalTime;

    public PackageImpl(int id, Locations startLocation, Locations endLocation, int weight, String contactInformation) {
        this.id = id;
        this.setStartLocation(startLocation);
        this.setEndLocation(endLocation);
        this.setWeight(weight);
        this.setContactInformation(contactInformation);
    }

    @Override
    public void setAssignedTruck(Truck assignedTruck) {
        this.assignedTruck = assignedTruck;
    }
    @Override
    public Truck getAssignedTruck() {
        return this.assignedTruck;
    }

    @Override
    public void unassign() { this.assignedTruck = null; }

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
        return this.assignedTruck != null;
    }

    @Override
    public void setEstimatedArrivalTime(LocalDateTime eventTime) {
        this.estimatedArrivalTime = eventTime;
    }

    @Override
    public LocalDateTime getEstimatedArrivalTime() {
        return this.estimatedArrivalTime;
    }

    @Override
    public int getId() {
        return this.id;
    }



    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(String.format("Package with id %d, start location %s, end location %s, weight %d, contact info %s ",
                this.id,
                this.startLocation.getDisplayName(),
                this.endLocation.getDisplayName(),
                this.weight,
                this.contactInformation));
        if (this.isAssigned()) {
            output.append(String.format("is assigned to truck %s with id %d.",
                    this.assignedTruck.getTruckType().getDisplayName(),
                    this.assignedTruck.getId()));
            output.append(String.format(" Estimated arrival time is: %s",Parsers.parseEventTimeToString(estimatedArrivalTime)));
        } else {
            output.append("is not assigned to a truck yet.");
        }
        return output.toString();
    }

    @Override
    public String toSaveString() {
        return String.join("|",
                String.valueOf(id),
                startLocation.getDisplayName(),
                endLocation.getDisplayName(),
                String.valueOf(weight),
                contactInformation,
                estimatedArrivalTime == null? "NONE" : Parsers.parseEventTimeToString(estimatedArrivalTime),
                assignedTruck == null ? "NONE" : String.valueOf(assignedTruck.getId()));
    }
}
