package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.utils.Parsers;
import com.austria.logistics.utils.Validators;
import com.austria.logistics.models.contracts.Package;

import java.util.List;

public class ShowPackage extends BaseCommand {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;

    public ShowPackage(Repository repository) {
        super(repository);
    }

    //EXPECTED STRING PACKAGE ID
    @Override
    protected String executeCommand(List<String> parameters) {
        User loggedUser = getRepository().getLoggedUser();

        if (loggedUser.getUserRole() != UserRole.MANAGER && loggedUser.getUserRole() != UserRole.EMPLOYEE) {
            return Constants.USER_NOT_MANAGER_AND_NOT_EMPLOYEE;
        }

        Validators.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);
        int pkgId = Parsers.parseToInteger("Package id", parameters.get(0));

        return showPackage(pkgId);
    }

    private String showPackage(int pkgId) {
        Repository repo = getRepository();

        Package pkgToPrint = repo.findElementById(repo.getPackages(), pkgId);
        User userToReceiveEmail = repo.findUserByEmail(pkgToPrint.getContactInformation());

        userToReceiveEmail.receiveLetter(pkgToPrint.toString());
        return pkgToPrint.toString();
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }
}
