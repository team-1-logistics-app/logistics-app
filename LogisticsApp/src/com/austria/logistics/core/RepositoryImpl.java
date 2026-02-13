package com.austria.logistics.core;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.*;
import com.austria.logistics.models.PackageImpl;
import com.austria.logistics.models.RouteImpl;
import com.austria.logistics.models.UserImpl;
import com.austria.logistics.models.contracts.*;
import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.models.enums.TruckType;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.models.vehicles.TruckImpl;
import com.austria.logistics.models.vehicles.contracts.Truck;
import com.austria.logistics.utils.Helpers;
import com.austria.logistics.utils.Parsers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class RepositoryImpl implements Repository {
    private static final String NO_LOGGED_IN_USER = "There is no logged in user.";
    private final static String NO_SUCH_USER = "There is no user with username %s!";
    private final static String USER_ALREADY_EXIST = "User %s already exist. Choose a different username!";


    private int nextId;
    private User loggedUser;

    private final List<Truck> trucks;
    private final List<Route> routes;
    private final List<Package> packages;
    private final List<User> users;


    public RepositoryImpl() {
        this.nextId = 0;
        this.loggedUser = null;
        trucks = new ArrayList<>();
        routes = new ArrayList<>();
        packages = new ArrayList<>();
        users = new ArrayList<>();
        for (int id = 1001; id <= 1010; id++) this.trucks.add(this.createTruck(id, TruckType.SCANIA));
        for (int id = 1011; id <= 1025; id++) this.trucks.add(this.createTruck(id, TruckType.MAN));
        for (int id = 1026; id <= 1040; id++) this.trucks.add(this.createTruck(id, TruckType.ACTROS));
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
    public List<User> getUsers() {
        return new ArrayList<>(this.users);
    }

    @Override
    public <E extends Identifiable> E findElementById(List<E> elements, int id) {
        return elements.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ElementNotFoundException(String.format(Constants.ELEMENT_NOT_FOUND_MESSAGE, id)));
    }

    private Truck createTruck(int id, TruckType truckType) {
        return new TruckImpl(id, truckType);
    }

    @Override
    public Route createRoute() {
        Route route = new RouteImpl(++this.nextId);
        this.routes.add(route);
        return route;
    }

    @Override
    public Route assignTruckToRoute(Truck truck, Route route) {
        if (route.getRouteLocations().size() < 2) {
            throw new RouteIsEmptyException(String.format(Constants.ROUTE_ASSIGN_ERROR_MESSAGE, route.getId()));
        }
        truck.assign();
        truck.setAssignedRoute(route);
        route.assignTruck(truck);
        return route;
    }

    @Override
    public void unassignTruckFromRoute(Truck truck, Route route) {
        if (truck.getAssignedRoute() == null || truck.getAssignedRoute().getId() != route.getId()) {
            throw new TruckNotAssignedToRouteException(String.format(Constants.TRUCK_NOT_ASSIGNED_TO_THIS_ROUTE_MESSAGE,
                    truck.getTruckType().getDisplayName(),
                    truck.getId(),
                    route.getId()));
        }
        truck.unassign();
        route.unassignTruck();
    }

    @Override
    public Truck assignPackageToTruck(Package pkg, Truck truck) {
        if (!truck.isAssigned()) {
            throw new TruckNotAssignedToRouteException(String.format(Constants.TRUCK_NOT_ASSIGNED_MESSAGE, truck.getTruckType().getDisplayName(), truck.getId()));
        }

        if (truck.getAssignedPackagesIdList().contains(pkg.getId()) || pkg.isAssigned()) {
            throw new PackageIsAlreadyAssignedException(String.format(Constants.PACKAGE_ALREADY_ASSIGNED_ERROR_MESSAGE, pkg.getId(), truck.getTruckType().getDisplayName(), truck.getId()));
        }

        Route route = this.findElementById(this.getRoutes(), truck.getAssignedRoute().getId());
        List<Location> routeLocations = route.getRouteLocations();

        Location startLocation = route.findByCity(pkg.getStartLocation());
        Location endLocation = route.findByCity(pkg.getEndLocation());

        int startLocationIndex = routeLocations.indexOf(startLocation);
        int endLocationIndex = routeLocations.indexOf(endLocation);

        if (startLocationIndex >= endLocationIndex) {
            throw new NoPathException(this.generatePackageAssignErrorMessage(pkg, route));
        }

        pkg.setAssignedTruck(truck);
        truck.addAssignedPackageId(pkg.getId());

        if (endLocationIndex > 0) {
            LocalDateTime pkgArrivalTime = endLocation.getEventTime();
            pkg.setEstimatedArrivalTime(pkgArrivalTime);
        }

        return truck;
    }

    @Override
    public void unassignPackageFromTruck(Package pkg, Truck truck) {
        if(truck.getAssignedPackagesIdList().contains(pkg.getId())){
            truck.removeAssignedPackageId(pkg.getId());
            truck.removeLoad(pkg.getWeight());
            pkg.unassign();
        }
    }

    private String generatePackageAssignErrorMessage(Package pkg, Route route) {
        return String.format(Constants.PACKAGE_ASSIGN_ERROR_MESSAGE,
                pkg.getId(),
                route.getId(),
                pkg.getStartLocation().getDisplayName(),
                pkg.getEndLocation().getDisplayName());
    }

    @Override
    public Package createPackage(Locations startLocation, Locations endLocation, int weight, String contactInformation) {
        Package pkg = new PackageImpl(++this.nextId, startLocation, endLocation, weight, contactInformation);
        this.packages.add(pkg);
        return pkg;
    }

    @Override
    public User addUser(User userToAdd) {
        if (users.contains(userToAdd)) {
            throw new UserAlreadyExistsException(String.format(USER_ALREADY_EXIST, userToAdd.getUsername()));
        }
        this.users.add(userToAdd);
        return userToAdd;
    }

    @Override
    public User getLoggedUser() {
        return this.loggedUser;
    }

    @Override
    public boolean hasLoggedUser() {
        return this.loggedUser != null;
    }

    @Override
    public User createUser(String username, String firstName, String lastName, String password, String email, UserRole userRole) {
        return new UserImpl(username, firstName, lastName, password, email, userRole);
    }

    @Override
    public void login(User username) {
        this.loggedUser = username;
    }

    @Override
    public void logout() {
        this.loggedUser = null;
    }

    @Override
    public User findUserByUsername(String username) {
        return this.getUsers().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format(Constants.USER_USERNAME_NOT_FOUND, username)));
    }

    @Override
    public User findUserByEmail(String email) {
        return this.getUsers().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format(Constants.USER_EMAIL_NOT_FOUND, email)));
    }

    @Override
    public String saveToFile() {
        try {
            Files.createDirectories(Path.of(Constants.STATE_DIR_TO_SAVE));

            Files.write(Constants.FILE_PATH_ROUTES, Parsers.parseCollectionToStringList(this.routes));
            Files.write(Constants.FILE_PATH_TRUCKS, Parsers.parseCollectionToStringList(this.trucks));
            Files.write(Constants.FILE_PATH_PACKAGES, Parsers.parseCollectionToStringList(this.packages));
            Files.write(Constants.FILE_PATH_USERS, Parsers.parseCollectionToStringList(this.users));

        } catch (IOException e) {
           throw new FailedToSaveToFileException(Constants.STATE_FAILED_TO_SAVE);
        }
        return Constants.STATE_SAVED_TO_FILE;
    }


    @Override
    public String loadFromFile() {
        Helpers.readFileLines(Constants.FILE_PATH_ROUTES, this.routes, Parsers::routeFromSaveString);
        Helpers.readFileLines(Constants.FILE_PATH_TRUCKS, this.trucks, line -> Parsers.truckFromSaveString(line, this));

        resolveRouteTruckReferences();

        Helpers.readFileLines(Constants.FILE_PATH_PACKAGES, this.packages, line -> Parsers.packageFromSaveString(line, this));
        Helpers.readFileLines(Constants.FILE_PATH_USERS, this.users, line -> Parsers.userFromSaveString(line, this));

        return Constants.STATE_LOADED_FROM_FILE;
    }


    private void resolveRouteTruckReferences() {
        this.routes.forEach(route -> {
            if (route.getLoadedTruckId() != -1) {
                Truck truck = this.findElementById(this.trucks, route.getLoadedTruckId());
                route.assignTruck(truck);
            }
        });
    }

}

