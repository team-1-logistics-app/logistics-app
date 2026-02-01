package com.austria.logistics.models.vehicles;

import com.austria.logistics.models.enums.TruckType;
import com.austria.logistics.models.vehicles.contracts.Truck;

public class TruckImpl implements Truck {
    private final int id;
    private final TruckType truckType;
    private boolean isAssigned;
    private int currentLoad;

    TruckImpl(int id, TruckType truckType){
        this.id = id;
        this.truckType = truckType;
        this.isAssigned = false;
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
    public int getCurrentLoad() {
        return this.currentLoad;
    }

    @Override
    public int getMaxCapacity() {
        return truckType.getCapacity();
    }

    @Override
    public void addLoad(int weight) {
        this.currentLoad += weight;
    }

    @Override
    public void assign() {
        this.isAssigned = true;
    }

    @Override
    public void unassign() {
        this.isAssigned =  false;
        this.currentLoad = 0;
    }

}
