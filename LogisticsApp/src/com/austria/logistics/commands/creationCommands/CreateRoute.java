package com.austria.logistics.commands.creationCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class CreateRoute extends BaseCommand {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 0;

    public CreateRoute(Repository repository) {
        super(repository);
    }

    //NO ARGUMENTS ARE EXPECTED
    @Override
    public String executeCommand(List<String> parameters) {
        User loggedUser = getRepository().getLoggedUser();

        if(loggedUser.getUserRole() != UserRole.EMPLOYEE){
            return Constants.USER_NOT_EMPLOYEE;
        }

        try {
            Validators.validateArgumentsCount(parameters,EXPECTED_NUMBER_OF_ARGUMENTS);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }

        return createRoute();
    }

    private String createRoute(){
        int id = getRepository().createRoute().getId();
        return String.format(Constants.ROUTE_CREATED_MESSAGE,id);
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }
}
