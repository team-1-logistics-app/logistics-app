package com.austria.logistics.core;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.ElementNotFoundException;
import com.austria.logistics.models.PackageImpl;
import com.austria.logistics.models.RouteImpl;
import com.austria.logistics.models.contracts.Identifiable;
import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.models.enums.TruckType;
import com.austria.logistics.models.vehicles.TruckImpl;
import com.austria.logistics.models.vehicles.contracts.Truck;

import java.util.ArrayList;
import java.util.List;

public class RepositoryImpl implements Repository {
    private int nextId;

    private final List<Truck> trucks = new ArrayList<>();;
    private final List<Route> routes = new ArrayList<>();;
    private final List<Package> packages = new ArrayList<>();;

    public RepositoryImpl(){
        this.nextId =0;
        for (int id = 1001; id <= 1010; id++) this.trucks.add(this.createTruck(id,TruckType.SCANIA));
        for (int id = 1011; id <= 1025; id++) this.trucks.add(this.createTruck(id,TruckType.MAN));
        for (int id = 1026; id <= 1040; id++) this.trucks.add(this.createTruck(id,TruckType.ACTROS));
    }

    @Override
    public List<Truck> getTrucks() {
        return new ArrayList<>(this.trucks);
    }

    @Override
    public List<Route> getRoutes() {
        return new ArrayList<>(this.routes);
    }

    @Override
    public List<Package> getPackages() {
        return new ArrayList<>(this.packages);
    }

    @Override
    public <E extends Identifiable> E findElementById(List<E> elements, int id) {
        return elements.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ElementNotFoundException(String.format(Constants.ELEMENT_NOT_FOUND_MESSAGE,id)));
    }

    private Truck createTruck(int id, TruckType truckType) {
        Truck truck = new TruckImpl(id,truckType);
        this.trucks.add(truck);
        return truck;
    }

    @Override
    public Route createRoute() {
        Route route = new RouteImpl(++this.nextId);
        this.routes.add(route);
        return route;
    }

    @Override
    public Route assignTruckToRoute(Truck truck, int id) {
        Route route = this.findElementById(this.getRoutes(),id);
        route.assignTruck(truck);
        return route;
    }

    @Override
    public Package createPackage(Locations startLocation, Locations endLocation, int weight, String contactInformation) {
        Package pkg = new PackageImpl(++this.nextId,startLocation,endLocation,weight,contactInformation);
        this.packages.add(pkg);
        return pkg;
    }
}

