package com.austria.logistics.commands.userCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.InvalidValueException;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.utils.Parsers;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class Register extends BaseCommand {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 5;

    public Register(Repository repository) {
        super(repository);
    }

    @Override
    protected String executeCommand(List<String> parameters) {
        if (getRepository().hasLoggedUser()) {
            return  String.format(Constants.USER_LOGGED_IN_ALREADY,getRepository().getLoggedUser().getUsername());
        }

        try {
            Validators.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }

        String username = parameters.get(0);
        String firstName = parameters.get(1);
        String lastName = parameters.get(2);
        String password = parameters.get(3);
        String email = parameters.get(4);
        UserRole userRole = UserRole.CUSTOMER;
        if (parameters.size() == EXPECTED_NUMBER_OF_ARGUMENTS + 1) {
            try {
                userRole = Parsers.tryParseEnum(parameters.get(5), UserRole.class, String.format(Constants.INVALID_ENUM_VALUE_FORMAT_MESSAGE, parameters.get(5)));
            } catch (IllegalArgumentException e) {
                return e.getMessage();
            }
        }
        return register(username, firstName, lastName, password, email, userRole);
    }

    private String register(String username, String firstName, String lastName, String password,String email, UserRole userRole) {
        Repository repo = getRepository();

        if (repo.getUsers().stream().anyMatch(user -> user.getUsername().equals(username))) {
            return String.format(Constants.USER_ALREADY_EXIST, username);
        }

        if (repo.getUsers().stream().anyMatch(user -> user.getEmail().equals(email))) {
            return String.format(Constants.USER_EMAIL_ALREADY_USED, email);
        }

        User user;
        try {
            user = repo.createUser(username, firstName, lastName, password,email, userRole);
            repo.addUser(user);
        }catch (IllegalArgumentException | InvalidValueException e){
            return e.getMessage();
        }
        return String.format(Constants.USER_REGISTERED, username);
    }

    @Override
    protected boolean requiresLogin() {
        return false;
    }
}
