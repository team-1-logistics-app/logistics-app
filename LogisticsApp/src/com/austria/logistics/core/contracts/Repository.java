package com.austria.logistics.core.contracts;

import com.austria.logistics.models.contracts.*;
import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.models.enums.TruckType;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.models.vehicles.contracts.Truck;

import java.time.LocalDateTime;
import java.util.List;

public interface Repository {
    List<Truck> getTrucks();
    List<Route> getRoutes();
    List<Package> getPackages();
    List<User> getUsers();

    <E extends Identifiable> E findElementById(List<E> elements, int id);

    Route createRoute();
    Route assignTruckToRoute(Truck truck, Route route);
    Truck assignPackageToTruck(Package pkg, Truck truck);
    Package createPackage(Locations startLocation, Locations endLocation, int weight, String contactInformation);
    User addUser(User userToAdd);
    User getLoggedUser();
    boolean hasLoggedUser();
    User createUser(String username,String firstName, String lastName, String password, UserRole userRole);
    void login(User username);
    void logout();
    User findUserByUsername(String username);

}
