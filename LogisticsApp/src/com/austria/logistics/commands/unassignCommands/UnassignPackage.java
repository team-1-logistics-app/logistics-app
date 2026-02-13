package com.austria.logistics.commands.unassignCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.ElementNotFoundException;
import com.austria.logistics.exceptions.InvalidValueException;
import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.models.vehicles.contracts.Truck;
import com.austria.logistics.utils.Parsers;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class UnassignPackage extends BaseCommand {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;
    private Repository repo = getRepository();

    public UnassignPackage(Repository repository) {
        super(repository);
    }
    //EXPECTED STRING PACKAGE ID
    @Override
    protected String executeCommand(List<String> parameters) {
        User loggedUser = getRepository().getLoggedUser();

        if (loggedUser.getUserRole() != UserRole.MANAGER && loggedUser.getUserRole() != UserRole.EMPLOYEE) {
            return Constants.USER_NOT_MANAGER_AND_NOT_EMPLOYEE;
        }

        Package pkg;
        try {
            Validators.validateArgumentsCount(parameters,EXPECTED_NUMBER_OF_ARGUMENTS);
            int pkgId = Parsers.parseToInteger("Package id",parameters.get(0));
            pkg = repo.findElementById(repo.getPackages(),pkgId);
        }catch (IllegalArgumentException | InvalidValueException | ElementNotFoundException e){
            return e.getMessage();
        }
        return unassignPackage(pkg);
    }

    private String unassignPackage(Package pkg){
        if(!pkg.isAssigned()){
            return String.format(Constants.PACKAGE_IS_NOT_ASSIGNED_MESSAGE,pkg.getId());
        }

        Truck truck = repo.findElementById(repo.getTrucks(),pkg.getAssignedTruck().getId());
        repo.unassignPackageFromTruck(pkg,truck);

        return String.format(Constants.PACKAGE_SUCCESSFULLY_UNASSIGNED_MESSAGE,pkg.getId());
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }
}
