package com.austria.logistics.commands.assignCommands;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.*;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.enums.TruckType;
import com.austria.logistics.models.vehicles.contracts.Truck;
import com.austria.logistics.utils.Parsers;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class AssignTruck implements Command {
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 2;
    public final Repository repository;

    public AssignTruck(Repository repository) {
        this.repository = repository;
    }

    //EXPECTED ARGUMENTS ARE STRING ROUTEID AND STRING TRUCKTYPE
    @Override
    public String execute(List<String> parameters) {
        TruckType truck;
        int id;

        Route route;

        try {
            Validators.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);
            id = Parsers.parseToInteger("Route id", parameters.get(0));
            route = this.repository.findElementById(this.repository.getRoutes(), id);
            truck = Parsers.parseTruck(parameters.get(1));
        } catch (IllegalArgumentException | InvalidValueException | ElementNotFoundException |
                 InvalidTruckTypeException e) {
            return e.getMessage();
        }
        return assignTruck(route, truck);
    }

    private String assignTruck(Route route, TruckType truckType) {
        Truck truck;
        int routeId;
        try {
            truck = this.repository.getTrucks().stream()
                    .filter(element -> element.getTruckType() == truckType && !element.isAssigned())
                    .findFirst()
                    .orElseThrow(() -> new NoAvailableTruckException(String.format(Constants.TRUCK_TYPE_NOT_AVAILABLE_MESSAGE, truckType.getDisplayName())));
            routeId = repository.assignTruckToRoute(truck, route).getId();
        } catch (NoAvailableTruckException | RouteIsEmptyException  e) {
            return e.getMessage();
        }

        return String.format(Constants.TRUCK_ASSIGNED_MESSAGE, truckType.getDisplayName(), truck.getId(), routeId);
    }
}
