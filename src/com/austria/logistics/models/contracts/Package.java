package com.austria.logistics.models.contracts;

import com.austria.logistics.models.enums.Locations;

public interface Package extends Identifiable {
    void setRoute(Route route);
    Route getRoute();

    void setStartLocation(Locations startLocation);
    Location getStartLocation();

    void setEndLocation(Locations endLocation);
    Location getEndLocation();

    void setWeight(int weight);
    int getWeight();

    void setContactInformation(String contactInformation);
    String getContactInformation();
}