package com.austria.logistics.commands.assignCommands;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.*;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.utils.Parsers;
import com.austria.logistics.utils.Validators;


import java.time.LocalDateTime;
import java.util.List;

public class AssignLocation implements Command {
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS_WHEN_ROUTE_ISEMPTY = 3;
    public static final int MIN_ARGUMENTS_COUNT  = 2;
    private final Repository repository;

    public AssignLocation(Repository repository){this.repository = repository;}

    @Override
    public String execute(List<String> parameters) {
        int routeId;
        Route route;
        Locations location;
        LocalDateTime eventTime= null;

        if(parameters.size() < MIN_ARGUMENTS_COUNT){
            return String.format(Constants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE, MIN_ARGUMENTS_COUNT,parameters.size());
        }

        try{
            routeId = Parsers.parseToInteger("Route id",parameters.get(0));
            route = this.repository.findElementById(this.repository.getRoutes(),routeId);
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
}
