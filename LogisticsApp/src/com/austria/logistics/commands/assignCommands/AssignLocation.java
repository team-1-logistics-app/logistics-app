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
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS_WHEN_ROUTE_ISEMPTY = 5;
    public static final int MIN_ARGUMENTS_COUNT  = 2;

    public AssignLocation(Repository repository) {
        super(repository);
    }

    @Override
    public String executeCommand(List<String> parameters) {
        User loggedUser = getRepository().getLoggedUser();

        if(loggedUser.getUserRole() != UserRole.MANAGER && loggedUser.getUserRole() != UserRole.EMPLOYEE){
            return Constants.USER_NOT_MANAGER_AND_NOT_EMPLOYEE;
        }

        int routeId;
        Route route;
        Locations location;
        LocalDateTime eventTime = null;

        try{
            Validators.validateArgumentsCount(parameters, MIN_ARGUMENTS_COUNT);
            routeId = Parsers.parseToInteger("Route id",parameters.get(0));
            route = getRepository().findElementById(getRepository().getRoutes(),routeId);
            location = Parsers.parseLocation(parameters.get(1));

            if(route.isRouteEmpty() && parameters.size() > MIN_ARGUMENTS_COUNT){
                Validators.validateArgumentsCount(parameters,EXPECTED_NUMBER_OF_ARGUMENTS_WHEN_ROUTE_ISEMPTY);
                String eventTimeString = String.join(" ",
                        parameters.get(2),
                        parameters.get(3),
                        parameters.get(4)
                );
                eventTime = Parsers.parseEventTimeToLocalDateTime(eventTimeString);
            }

            return parameters.size() == EXPECTED_NUMBER_OF_ARGUMENTS_WHEN_ROUTE_ISEMPTY? addFirstLocation(route,location,eventTime) : addAnyOtherLocation(route,location);
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
