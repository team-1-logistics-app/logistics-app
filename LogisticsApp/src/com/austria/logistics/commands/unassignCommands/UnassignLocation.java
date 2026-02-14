package com.austria.logistics.commands.unassignCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.FailedToRemoveLocationException;
import com.austria.logistics.exceptions.NotLoggedInException;
import com.austria.logistics.models.contracts.Location;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.CityName;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.utils.Parsers;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class UnassignLocation extends BaseCommand {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 2;
    private Repository repo = getRepository();

    public UnassignLocation(Repository repository) {
        super(repository);
    }

    //EXPECTED STRING ROUTE ID AND STRING CITY
    @Override
    protected String executeCommand(List<String> parameters) {
        User loggedUser = getRepository().getLoggedUser();

        if (loggedUser.getUserRole() != UserRole.MANAGER && loggedUser.getUserRole() != UserRole.EMPLOYEE) {
            throw new NotLoggedInException(Constants.USER_NOT_MANAGER_AND_NOT_EMPLOYEE);
        }
        Validators.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        int routeId = Parsers.parseToInteger("Route id", parameters.get(0));
        CityName location = Parsers.parseLocation(parameters.get(1));
        Route route = repo.findElementById(repo.getRoutes(), routeId);


        return unassignLocation(route, location);
    }

    private String unassignLocation(Route route, CityName location) {
        Location locationToCheck = route.findByCity(location);

        if (route.getRouteLocations().indexOf(locationToCheck) == 0 && route.getRouteLocations().size() > 1) {
            throw new FailedToRemoveLocationException(Constants.ROUTE_REMOVE_STARTLOCATION_ERROR_MESSAGE);
        }

        return route.removeLocationFromRoute(location);
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }
}
