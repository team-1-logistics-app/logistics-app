package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.ElementNotFoundException;
import com.austria.logistics.exceptions.InvalidValueException;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.utils.Parsers;
import com.austria.logistics.utils.Validators;
import com.austria.logistics.models.contracts.Package;

import java.util.List;

public class ShowPackage extends BaseCommand {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;

    public ShowPackage(Repository repository){
        super(repository);
    }


    @Override
    protected String executeCommand(List<String> parameters) {
        User loggedUser = getRepository().getLoggedUser();

        if(loggedUser.getUserRole() != UserRole.EMPLOYEE){
            return Constants.USER_NOT_EMPLOYEE;
        }
        int pkgId;
        try {
            Validators.validateArgumentsCount(parameters,EXPECTED_NUMBER_OF_ARGUMENTS);
            pkgId = Parsers.parseToInteger("Package id", parameters.get(0));
        }catch (IllegalArgumentException | InvalidValueException e){
            return e.getMessage();
        }

        return showPackage(pkgId);
    }

    private String showPackage(int pkgId){
        Repository repo = getRepository();
        Package pkgToPrint;
        try {
           pkgToPrint = repo.findElementById(repo.getPackages(), pkgId);
        }catch (ElementNotFoundException e){
            return e.getMessage();
        }
        return pkgToPrint.toString();
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }
}
