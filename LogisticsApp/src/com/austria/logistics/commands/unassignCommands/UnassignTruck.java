package com.austria.logistics.commands.unassignCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.ElementNotFoundException;
import com.austria.logistics.exceptions.InvalidValueException;
import com.austria.logistics.exceptions.TruckNotAssignedToRouteException;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.models.vehicles.contracts.Truck;
import com.austria.logistics.utils.Parsers;

import java.util.List;

public class UnassignTruck extends BaseCommand {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 2;

    public UnassignTruck(Repository repository) {
        super(repository);
    }

    @Override
    protected String executeCommand(List<String> parameters) {
        User loggedUser = getRepository().getLoggedUser();

        if (loggedUser.getUserRole() != UserRole.MANAGER || loggedUser.getUserRole() != UserRole.EMPLOYEE) {
            return Constants.USER_NOT_MANAGER_AND_NOT_EMPLOYEE;
        }

        Repository repo = getRepository();
        int truckId;
        int routeId;
        Truck truck;
        Route route;

        try {
            truckId = Parsers.parseToInteger("Truck id", parameters.get(0));
            routeId = Parsers.parseToInteger("Route id", parameters.get(1));
            truck = repo.findElementById(repo.getTrucks(), truckId);
            route = repo.findElementById(repo.getRoutes(), routeId);
        } catch (InvalidValueException | ElementNotFoundException e) {
            return e.getMessage();
        }

        return unassignTruck(truck, route);
    }

    private String unassignTruck(Truck truck, Route route) {
        try {
            getRepository().unassignTruckFromRoute(truck, route);
        } catch (TruckNotAssignedToRouteException e) {
            return e.getMessage();
        }
        return String.format(Constants.TRUCK_SUCCESSFULLY_UNASSIGNED_FROM_ROUTE,
                truck.getTruckType().getDisplayName(),
                truck.getId(),
                route.getId());
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }
}
