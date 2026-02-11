package com.austria.logistics.models.vehicles;

import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.contracts.Savealbe;
import com.austria.logistics.models.enums.TruckType;
import com.austria.logistics.models.vehicles.contracts.Truck;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TruckImpl implements Truck {
    private final int id;
    private final TruckType truckType;
    private final List<Integer> assignedPackagesIdList;
    private boolean isAssigned;
    private Route assignedRoute;
    private int currentLoad;

    public TruckImpl(int id, TruckType truckType){
        this.id = id;
        this.truckType = truckType;
        this.isAssigned = false;
        this.assignedPackagesIdList = new ArrayList<>();
        this.currentLoad = 0;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public TruckType getTruckType() {
        return this.truckType;
    }

    @Override
    public boolean isAssigned() {
        return this.isAssigned;
    }

    @Override
    public int getCurrentWeight() {
        return this.currentLoad;
    }

    @Override
    public int getMaxCapacity() {
        return truckType.getCapacity();
    }

    @Override
    public Route getAssignedRoute() {
       return this.assignedRoute;
    }

    @Override
    public List<Integer> getAssignedPackagesIdList() {
        return new ArrayList<>(this.assignedPackagesIdList);
    }

    @Override
    public void addAssignedPackageId(int id) {
        this.assignedPackagesIdList.add(id);
    }

    @Override
    public void addLoad(int weight) {
        this.currentLoad += weight;
    }

    @Override
    public void setAssignedRoute(Route assignedRoute) {
        this.assignedRoute = assignedRoute;
    }

    @Override
    public void assign() {
        this.isAssigned = true;
    }

    @Override
    public void unassign() {
        this.isAssigned =  false;
        this.assignedRoute = null;
        this.currentLoad = 0;
    }

    @Override
    public String toSaveString() {
        return String.join("|",
                String.valueOf(id),
                truckType.getDisplayName(),
               assignedPackagesIdList.isEmpty()? "NONE" : assignedPackagesIdList.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")),
                String.valueOf(isAssigned),
                assignedRoute == null? "NONE" : String.valueOf(assignedRoute.getId()),
                String.valueOf(currentLoad));
    }
}
