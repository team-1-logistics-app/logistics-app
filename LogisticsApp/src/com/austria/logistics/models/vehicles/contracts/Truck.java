package com.austria.logistics.models.vehicles.contracts;

import com.austria.logistics.models.contracts.Identifiable;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.enums.TruckType;

import java.util.List;

public interface Truck extends Identifiable {
    TruckType getTruckType();
    boolean isAssigned();
    int getCurrentWeight();
    int getMaxCapacity();
    Route getAssignedRoute();
    List<Integer> getAssignedPackagesIdList();

    void addAssignedPackageId(int id);
    void addLoad(int weight);
    void setAssignedRoute(Route assignedRoute);
    void assign();
    void unassign();

}


