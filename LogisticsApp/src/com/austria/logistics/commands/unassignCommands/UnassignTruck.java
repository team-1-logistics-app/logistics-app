package com.austria.logistics.commands.unassignCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.NotLoggedInException;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.models.vehicles.contracts.Truck;
import com.austria.logistics.utils.Parsers;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class UnassignTruck extends BaseCommand {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 2;

    public UnassignTruck(Repository repository) {
        super(repository);
    }

    //EXPECTED STRING TRUCK ID AND STRING ROUTE ID
    @Override
    protected String executeCommand(List<String> parameters) {
        Repository repo = getRepository();
        User loggedUser = getRepository().getLoggedUser();

        if (loggedUser.getUserRole() != UserRole.MANAGER && loggedUser.getUserRole() != UserRole.EMPLOYEE) {
            throw new NotLoggedInException(Constants.USER_NOT_MANAGER_AND_NOT_EMPLOYEE);
        }
        Validators.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        int truckId = Parsers.parseToInteger("Truck id", parameters.get(0));
        int routeId = Parsers.parseToInteger("Route id", parameters.get(1));
        Truck truck = repo.findElementById(repo.getTrucks(), truckId);
        Route route = repo.findElementById(repo.getRoutes(), routeId);

        return unassignTruck(truck, route);
    }

    private String unassignTruck(Truck truck, Route route) {
        getRepository().unassignTruckFromRoute(truck, route);

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
