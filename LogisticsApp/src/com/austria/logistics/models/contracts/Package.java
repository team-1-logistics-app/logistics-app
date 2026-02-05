package com.austria.logistics.models.contracts;

import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.models.vehicles.contracts.Truck;

public interface Package extends Identifiable {
    void setAssignedToTruck(Truck assignedToTruck);
    Truck getAssignedToTruck();

    void setStartLocation(Locations startLocation);
    Locations getStartLocation();

    void setEndLocation(Locations endLocation);
    Locations getEndLocation();

    void setWeight(int weight);
    int getWeight();

    void setContactInformation(String contactInformation);
    String getContactInformation();

    boolean isAssigned();
}