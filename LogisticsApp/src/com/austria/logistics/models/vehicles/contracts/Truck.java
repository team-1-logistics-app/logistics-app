package com.austria.logistics.models.vehicles.contracts;

import com.austria.logistics.models.contracts.Identifiable;
import com.austria.logistics.models.enums.TruckType;

public interface Truck extends Identifiable {
    TruckType getTruckType();
    boolean isAssigned();
    int getCurrentLoad();
    int getMaxCapacity();

    void addLoad(int weight);
    void assign();
    void unassign();
}


