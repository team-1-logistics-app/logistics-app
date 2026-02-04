package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.ElementNotFoundException;
import com.austria.logistics.exceptions.InvalidValueException;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.utils.Parsers;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class ShowRoute implements Command {
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;
    private final Repository repository;

    public ShowRoute(Repository repository){this.repository = repository;}

    @Override
    public String execute(List<String> parameters) {
        int id;
        Route route;
        try{
            Validators.validateArgumentsCount(parameters,EXPECTED_NUMBER_OF_ARGUMENTS);
            id = Parsers.parseToInteger("Route id", parameters.get(0));
            route = this.repository.findElementById(this.repository.getRoutes(),id);
        }catch (IllegalArgumentException | InvalidValueException | ElementNotFoundException e){
            return e.getMessage();
        }

        return showRoute(route);
    }

    private String showRoute(Route route){
        StringBuilder output = new StringBuilder();
        output.append(String.format("Current schedule for route with id %d:\n",route.getId()));
        route.getRouteLocations().stream().forEach(location -> {
            output.append("City: " + location.getLocation().getDisplayName() + " Scheduled time: " + location.getEventTimeAsString() + "\n");
        });
        return output.toString();
    }
}
