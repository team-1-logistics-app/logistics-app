package com.austria.logistics.models.enums;

public enum TruckType {
    SCANIA("Scania", 42000, 8000),
    MAN("Man", 37000,10000),
    ACTROS("Actros", 26000, 13000);

    private final String displayName;
    private final int capacity;
    private final int maxRange;

    TruckType(String displayName, int capacity, int maxRange){
        this.displayName = displayName;
        this.capacity = capacity;
        this.maxRange = maxRange;
    }

    public String getDisplayName() {return this.displayName;}
    public int getCapacity() {return this.capacity;}
    public int getMaxRange() {return this.maxRange;}

}
