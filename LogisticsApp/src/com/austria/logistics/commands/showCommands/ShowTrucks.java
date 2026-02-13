package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.models.vehicles.contracts.Truck;

import java.util.List;

public class ShowTrucks extends BaseCommand {

    public ShowTrucks(Repository repository) {
        super(repository);}
    //NO ARGUMENTS ARE EXPECTED
    @Override
    public String executeCommand(List<String> parameters) {
        User loggedUser = getRepository().getLoggedUser();

        if(loggedUser.getUserRole() != UserRole.MANAGER && loggedUser.getUserRole() != UserRole.EMPLOYEE){
            return Constants.USER_NOT_MANAGER_AND_NOT_EMPLOYEE;
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
