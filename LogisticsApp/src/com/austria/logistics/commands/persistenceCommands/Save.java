package com.austria.logistics.commands.persistenceCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;

import java.util.List;

public class Save extends BaseCommand {

    public Save(Repository repository) {
        super(repository);
    }

    @Override
    protected String executeCommand(List<String> parameters) {
        User loggedUser = getRepository().getLoggedUser();

        if(loggedUser.getUserRole() != UserRole.EMPLOYEE){
            return Constants.USER_NOT_EMPLOYEE;
        }

        return getRepository().saveToFile();
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }

}
