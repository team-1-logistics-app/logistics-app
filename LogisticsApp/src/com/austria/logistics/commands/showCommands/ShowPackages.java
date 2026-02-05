package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class ShowPackages implements Command {
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 0;
    private final Repository repository;

    public ShowPackages(Repository repository) {
        this.repository = repository;
    }

    @Override
    public String execute(List<String> parameters) {
        try {
            Validators.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        return showPackages();
    }

    private String showPackages() {
        StringBuilder output = new StringBuilder();
        List<Package> packages = this.repository.getPackages();
        if (!packages.isEmpty()) {
            packages.forEach(pkg -> {
                output.append(String.format("Package with id %d, start location %s, end location %s, weight %d, contact info %s ",
                        pkg.getId(),
                        pkg.getStartLocation().getDisplayName(),
                        pkg.getEndLocation().getDisplayName(),
                        pkg.getWeight(),
                        pkg.getContactInformation()));

                if (pkg.isAssigned()) {
                    output.append(String.format("is assigned to truck %s with id %d\n",
                            pkg.getAssignedToTruck().getTruckType().getDisplayName(),
                            pkg.getAssignedToTruck().getId()));
                } else {
                    output.append("is not assigned to a truck yet.\n");
                }
            });
        } else {
            output.append("No packages in the repo created yet.\n");
        }
        return output.toString();
    }
}
