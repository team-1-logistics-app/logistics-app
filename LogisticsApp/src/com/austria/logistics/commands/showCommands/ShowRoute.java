package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.models.vehicles.contracts.Truck;
import com.austria.logistics.utils.Parsers;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class ShowRoute extends BaseCommand {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;

    public ShowRoute(Repository repository) {
        super(repository);
    }

    //EXPECTED STRING ROUTE ID
    @Override
    public String executeCommand(List<String> parameters) {
        User loggedUser = getRepository().getLoggedUser();

        if (loggedUser.getUserRole() != UserRole.MANAGER && loggedUser.getUserRole() != UserRole.EMPLOYEE) {
            return Constants.USER_NOT_MANAGER_AND_NOT_EMPLOYEE;
        }

        Validators.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);
        int id = Parsers.parseToInteger("Route id", parameters.get(0));
        Route route = getRepository().findElementById(getRepository().getRoutes(), id);

        return showRoute(route);
    }

    private String showRoute(Route route) {
        StringBuilder output = new StringBuilder();
        output.append(String.format("Current schedule for route with id %d:\n", route.getId()));
        if (route.hasAssignedTruck()) {
            Truck assignedTruck = route.getAssignedTruck();
            output.append(String.format("The route has assigned truck %s with id %d.\n", assignedTruck.getTruckType().getDisplayName(), assignedTruck.getId()));
        } else {
            output.append("No assigned truck to the route.\n");
        }
        route.getRouteLocations().forEach(location -> {
            output.append(String.format("City: %s,", location.getLocation().getDisplayName()))
                    .append(String.format(" Scheduled time: %s\n", location.getEventTimeAsString()));
        });
        if (route.getRouteLocations().isEmpty()) {
            output.append("No locations added to the route yet.\n");
        }
        return output.toString();
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }
}
