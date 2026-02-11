package com.austria.logistics.commands.assignCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.*;
import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.models.vehicles.contracts.Truck;
import com.austria.logistics.utils.Parsers;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class AssignPackage extends BaseCommand {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 2;

    public AssignPackage(Repository repository) {
        super(repository);
    }


    //EXPECTED ARGUMENTS ARE STRING PACKAGEID AND STRING TRUCKID
    @Override
    public String executeCommand(List<String> parameters) {
        User loggedUser = getRepository().getLoggedUser();

        if(loggedUser.getUserRole() != UserRole.MANAGER && loggedUser.getUserRole() != UserRole.EMPLOYEE){
            return Constants.USER_NOT_MANAGER_AND_NOT_EMPLOYEE;
        }

        int packageId;
        int truckId;

        Package pkg;
        Truck truck;
        String result;
        try {
            Validators.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);
            packageId = Parsers.parseToInteger("Package id", parameters.get(0));
            truckId = Parsers.parseToInteger("Truck id", parameters.get(1));
            pkg = getRepository().findElementById(getRepository().getPackages(), packageId);
            truck = getRepository().findElementById(getRepository().getTrucks(), truckId);
            result = assignPackage(pkg, truck);
        } catch (IllegalArgumentException | InvalidValueException | ElementNotFoundException |
                 MaxCapacityReachedException e) {
            return e.getMessage();
        }

        return result;
    }

    private String assignPackage(Package pkg, Truck truck) {
        if (truck.getCurrentWeight() + pkg.getWeight() > truck.getMaxCapacity()) {
            throw new MaxCapacityReachedException(String.format(Constants.TRUCK_MAXCAPACITY_REACHED_MESSAGE, truck.getTruckType().getDisplayName(), truck.getId()));
        }

        truck.addLoad(pkg.getWeight());

        int truckId;
        try {
            truckId = getRepository().assignPackageToTruck(pkg, truck).getId();
        } catch (ElementNotFoundException |
                 LocationNotFoundException |
                 TruckNotAssignedToRouteException |
                 NoPathException |
                 PackageIsAlreadyAssignedException e) {
            return e.getMessage();
        }

        return String.format(Constants.PACKAGE_ASSIGNED_MESSAGE, pkg.getId(), truck.getTruckType().getDisplayName(), truckId);
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }
}
