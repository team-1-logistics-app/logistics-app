package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;

import java.util.List;

public class ShowUsers extends BaseCommand {
    public ShowUsers(Repository repository) {
        super(repository);
    }
    //NO ARGUMENTS ARE EXPECTED
    @Override
    protected String executeCommand(List<String> parameters) {
        User loggedUser = getRepository().getLoggedUser();

        if(loggedUser.getUserRole() != UserRole.MANAGER){
            return Constants.USER_NOT_MANAGER;
        }

        return showUsers();
    }

    private String showUsers(){
        StringBuilder output = new StringBuilder();
        Repository repo = getRepository();
        repo.getUsers().forEach(user -> output.append(user.toString()).append(System.lineSeparator()));
        return output.toString();
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }
}
