package com.austria.logistics.commands.assignCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.*;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.TruckType;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.models.vehicles.contracts.Truck;
import com.austria.logistics.utils.Parsers;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class AssignTruck extends BaseCommand {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 2;

    public AssignTruck(Repository repository) {
        super(repository);
    }

    //EXPECTED ARGUMENTS ARE STRING ROUTEID AND STRING TRUCKTYPE
    @Override
    public String executeCommand(List<String> parameters) {
        User loggedUser = getRepository().getLoggedUser();

        if (loggedUser.getUserRole() != UserRole.MANAGER && loggedUser.getUserRole() != UserRole.EMPLOYEE) {
            throw new NotLoggedInException(Constants.USER_NOT_MANAGER_AND_NOT_EMPLOYEE);
        }

        Validators.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);
        int id = Parsers.parseToInteger("Route id", parameters.get(0));
        Route route = getRepository().findElementById(getRepository().getRoutes(), id);
        TruckType truck = Parsers.parseTruck(parameters.get(1));

        return assignTruck(route, truck);
    }

    private String assignTruck(Route route, TruckType truckType) {
        if (route.getAssignedTruck() != null) {
            throw new TruckAlreadyAssignedException(String.format(Constants.ROUTE_ALREADY_HAVE_ASSIGNED_TRUCK_MESSAGE,route.getId()));
        }

        Truck truck = getRepository().getTrucks().stream()
                    .filter(element -> element.getTruckType() == truckType && !element.isAssigned())
                    .findFirst()
                    .orElseThrow(() -> new NoAvailableTruckException(String.format(Constants.TRUCK_TYPE_NOT_AVAILABLE_MESSAGE, truckType.getDisplayName())));

        int routeId = getRepository().assignTruckToRoute(truck, route).getId();

        return String.format(Constants.TRUCK_ASSIGNED_MESSAGE, truckType.getDisplayName(), truck.getId(), routeId);
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }
}
