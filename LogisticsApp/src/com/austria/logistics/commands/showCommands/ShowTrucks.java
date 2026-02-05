package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.vehicles.contracts.Truck;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class ShowTrucks implements Command {
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 0;
    private final Repository repository;

    public ShowTrucks(Repository repository) {this.repository = repository;}

    @Override
    public String execute(List<String> parameters) {
        try {
            Validators.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }

        return showTrucks();
    }

    private String showTrucks() {
        StringBuilder output = new StringBuilder();
        List<Truck> trucks = this.repository.getTrucks();

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
}
