package com.austria.logistics.core.contracts;

import com.austria.logistics.models.contracts.Identifiable;
import com.austria.logistics.models.contracts.Location;
import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.models.enums.TruckType;
import com.austria.logistics.models.vehicles.contracts.Truck;

import java.util.List;

public interface Repository {
    List<Truck> getTrucks();
    List<Route> getRoutes();
    List<Package> getPackages();

    <E extends Identifiable> E findElementById(List<E> elements, int id);

    Route createRoute();
    Route assignTruckToRoute(Truck truck, Route route);
    Truck assignPackageToTruck(Package pkg, Truck truck);
    Package createPackage(Locations startLocation, Locations endLocation, int weight, String contactInformation);
}
