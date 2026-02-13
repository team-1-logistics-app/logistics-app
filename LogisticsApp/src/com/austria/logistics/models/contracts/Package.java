package com.austria.logistics.models.contracts;

import com.austria.logistics.models.enums.CityName;
import com.austria.logistics.models.vehicles.contracts.Truck;

import java.time.LocalDateTime;

public interface Package extends Identifiable, Printable, Savealbe {
    void setAssignedTruck(Truck assignedTruck);
    Truck getAssignedTruck();
    void unassign();

    void setStartLocation(CityName startLocation);
    CityName getStartLocation();

    void setEndLocation(CityName endLocation);
    CityName getEndLocation();

    void setWeight(int weight);
    int getWeight();

    void setContactInformation(String contactInformation);
    String getContactInformation();

    boolean isAssigned();
    void setEstimatedArrivalTime(LocalDateTime eventTime);
    LocalDateTime getEstimatedArrivalTime();
}