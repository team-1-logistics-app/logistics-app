package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.NotLoggedInException;
import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class ShowPackages extends BaseCommand {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 0;

    public ShowPackages(Repository repository) {
        super(repository);
    }

    //NO ARGUMENTS ARE EXPECTED
    @Override
    public String executeCommand(List<String> parameters) {
        User loggedUser = getRepository().getLoggedUser();

        if (loggedUser.getUserRole() != UserRole.MANAGER && loggedUser.getUserRole() != UserRole.EMPLOYEE) {
            throw new NotLoggedInException(Constants.USER_NOT_MANAGER_AND_NOT_EMPLOYEE);
        }

        Validators.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);

        return showPackages();
    }

    private String showPackages() {
        StringBuilder output = new StringBuilder();
        List<Package> packages = getRepository().getPackages();
        if (!packages.isEmpty()) {
            packages.forEach(pkg ->
                    output.append(pkg.toString()).append(System.lineSeparator()));
        } else {
            output.append("No packages in the repo created yet.").append(System.lineSeparator());
        }
        return output.toString();
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }
}
