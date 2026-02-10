package com.austria.logistics.commands.assignCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.*;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.utils.Parsers;
import com.austria.logistics.utils.Validators;


import java.time.LocalDateTime;
import java.util.List;

public class AssignLocation extends BaseCommand{
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS_WHEN_ROUTE_ISEMPTY = 3;
    public static final int MIN_ARGUMENTS_COUNT  = 2;

    public AssignLocation(Repository repository) {
        super(repository);
    }

    @Override
    public String executeCommand(List<String> parameters) {
        User loggedUser = getRepository().getLoggedUser();

        if(loggedUser.getUserRole() != UserRole.EMPLOYEE){
            return Constants.USER_NOT_EMPLOYEE;
        }

        int routeId;
        Route route;
        Locations location;
        LocalDateTime eventTime= null;

        if(parameters.size() < MIN_ARGUMENTS_COUNT){
            return String.format(Constants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE, MIN_ARGUMENTS_COUNT,parameters.size());
        }

        try{
            routeId = Parsers.parseToInteger("Route id",parameters.get(0));
            route = getRepository().findElementById(getRepository().getRoutes(),routeId);
            if(route.isRouteEmpty()){
                Validators.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS_WHEN_ROUTE_ISEMPTY);
                eventTime = Parsers.parseEventTime(parameters.get(1));
            } else {
                Validators.validateArgumentsCount(parameters,MIN_ARGUMENTS_COUNT);
            }
            location = Parsers.parseLocation(parameters.get(2));

            return parameters.size() == 3? addFirstLocation(route,location,eventTime) : addAnyOtherLocation(route,location);
        } catch (InvalidValueException |
                 ElementNotFoundException |
                 IllegalArgumentException |
                 InvalidTimeFormatException |
                 InvalidLocationException |
                 InvalidLocationRouteException |
                 RouteIsEmptyException |
                 RouteIsNotEmptyException e){
            return e.getMessage();
        }
    }

    /// WILL REWORK THIS CORRECT CONNECTION IS REPO->ROUTE
    private String addFirstLocation(Route route, Locations location, LocalDateTime eventTime){
       return route.addFirstLocationToRoute(location,eventTime);
    }

    private String addAnyOtherLocation(Route route, Locations location){
        return route.addLocationToRoute(location);
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }

}
