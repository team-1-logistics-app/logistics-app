package com.austria.logistics.commands.unassignCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.ElementNotFoundException;
import com.austria.logistics.exceptions.InvalidLocationException;
import com.austria.logistics.exceptions.InvalidValueException;
import com.austria.logistics.exceptions.LocationNotFoundException;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.Locations;
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

    @Override
    protected String executeCommand(List<String> parameters) {
        User loggedUser = getRepository().getLoggedUser();

        if (loggedUser.getUserRole() != UserRole.MANAGER && loggedUser.getUserRole() != UserRole.EMPLOYEE) {
            return Constants.USER_NOT_MANAGER_AND_NOT_EMPLOYEE;
        }

        Route route;
        Locations location;
        try {
            Validators.validateArgumentsCount(parameters,EXPECTED_NUMBER_OF_ARGUMENTS);
            int routeId = Parsers.parseToInteger("Route id",parameters.get(0));
            location = Parsers.parseLocation(parameters.get(0));
            route = repo.findElementById(repo.getRoutes(),routeId);
        }catch (IllegalArgumentException |
                InvalidValueException |
                InvalidLocationException |
                ElementNotFoundException e){

            return  e.getMessage();
        }

        return unassignLocation(route,location);
    }

    private String unassignLocation(Route route, Locations location){
        try{
            route.findByCity(location);
        }catch (LocationNotFoundException e){
            return e.getMessage();
        }

        return route.removeLocationFromRoute(location);
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }
}
