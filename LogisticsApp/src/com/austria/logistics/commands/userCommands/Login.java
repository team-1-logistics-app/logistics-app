package com.austria.logistics.commands.userCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class Login extends BaseCommand {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 2;

    public Login(Repository repository) {
        super(repository);
    }

    //EXPECTED STRING USERNAME AND STRING PASSWORD
    @Override
    protected String executeCommand(List<String> parameters) {
        if (getRepository().hasLoggedUser()) {
            return String.format(Constants.USER_LOGGED_IN_ALREADY, getRepository().getLoggedUser().getUsername());
        }

        Validators.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        String username = parameters.get(0);
        String password = parameters.get(1);

        return login(username, password);
    }

    private String login(String username, String password) {
        Repository repo = getRepository();
        User user = repo.findUserByUsername(username);

        if (!user.getPassword().equals(password)) {
            return Constants.USER_PASSWORD_MISMATCH;
        }
        repo.login(user);

        return String.format(Constants.USER_LOGGED_IN, username);
    }

    @Override
    protected boolean requiresLogin() {
        return false;
    }
}
