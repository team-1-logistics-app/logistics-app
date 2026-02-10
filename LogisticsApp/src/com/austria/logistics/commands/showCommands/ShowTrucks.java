package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.vehicles.contracts.Truck;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class ShowTrucks extends BaseCommand {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 0;

    public ShowTrucks(Repository repository) {
        super(repository);}

    @Override
    public String executeCommand(List<String> parameters) {
        try {
            Validators.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }

        return showTrucks();
    }

    private String showTrucks() {
        StringBuilder output = new StringBuilder();
        List<Truck> trucks = getRepository().getTrucks();

        trucks.forEach(truck -> {
            output.append(truck.getTruckType().getDisplayName())
                    .append(String.format(" with id %d",truck.getId()));

            if (truck.isAssigned()) {
                output.append(String.format(" is assigned to route with id %d, ", truck.getAssignedRoute().getId()))
                        .append(String.format("current weight is %d kg and max capacity is %d kg", truck.getCurrentWeight(), truck.getMaxCapacity()));

            } else {
                output.append(" is not assigned.");
            }
            output.append("\n");
        });

        return output.toString();
    }
    @Override
    protected boolean requiresLogin() {
        return true;
    }

}
