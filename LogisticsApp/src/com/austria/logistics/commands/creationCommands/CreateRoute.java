package com.austria.logistics.commands.creationCommands;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class CreateRoute implements Command {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 0;
    private final Repository repository;
    public CreateRoute(Repository repository){this.repository = repository;}

    //NO ARGUMENTS ARE EXPECTED
    @Override
    public String execute(List<String> parameters) {
        try {
            Validators.validateArgumentsCount(parameters,EXPECTED_NUMBER_OF_ARGUMENTS);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }

        return createRoute();
    }

    private String createRoute(){
        int id = this.repository.createRoute().getId();
        return String.format(Constants.ROUTE_CREATED_MESSAGE,id);
    }
}
