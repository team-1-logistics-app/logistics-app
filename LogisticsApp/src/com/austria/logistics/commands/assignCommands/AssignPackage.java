package com.austria.logistics.commands.assignCommands;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.ElementNotFoundException;
import com.austria.logistics.exceptions.InvalidValueException;
import com.austria.logistics.exceptions.MaxCapacityReachedException;
import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.models.vehicles.contracts.Truck;
import com.austria.logistics.utils.Parsers;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class AssignPackage implements Command {
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 2;
    private final Repository repository;

    public AssignPackage(Repository repository){this.repository = repository;}


    @Override
    public String execute(List<String> parameters) {
        int packageId;
        int truckId;
        Package pkg;
        Truck truck;
        String result;
        try {
            Validators.validateArgumentsCount(parameters,EXPECTED_NUMBER_OF_ARGUMENTS);
            packageId = Parsers.parseToInteger("Package id",parameters.get(0));
            truckId = Parsers.parseToInteger("Truck id",parameters.get(1));
            pkg = this.repository.findElementById(this.repository.getPackages(),packageId);
            truck = this.repository.findElementById(this.repository.getTrucks(), truckId);
            result = assignPackage(pkg,truck);
        }catch(IllegalArgumentException | InvalidValueException | ElementNotFoundException | MaxCapacityReachedException e){
            return e.getMessage();
        }

        return result;
    }

    private String assignPackage(Package pkg, Truck truck){
        if(truck.getCurrentLoad() + pkg.getWeight() > truck.getMaxCapacity()){
            throw new MaxCapacityReachedException(String.format(Constants.TRUCK_MAXCAPACITY_REACHED_MESSAGE,truck.getTruckType().getDisplayName(), truck.getId()));
        }

        pkg.setTruck(truck);
        truck.addAssignedPackageId(pkg.getId());

        return String.format(Constants.PACKAGE_ASSIGNED_MESSAGE, pkg.getId(), truck.getTruckType().getDisplayName(), truck.getId());
    }
}
