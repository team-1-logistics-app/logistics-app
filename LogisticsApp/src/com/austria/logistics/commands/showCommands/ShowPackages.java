package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class ShowPackages extends BaseCommand {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 0;

    public ShowPackages(Repository repository) {
        super(repository);
    }


    @Override
    public String executeCommand(List<String> parameters) {
        try {
            Validators.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        return showPackages();
    }

    private String showPackages() {
        StringBuilder output = new StringBuilder();
        List<Package> packages = getRepository().getPackages();
        if (!packages.isEmpty()) {
            packages.forEach(pkg -> output.append(pkg.toString()));
        } else {
            output.append("No packages in the repo created yet.\n");
        }
        return output.toString();
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }
}
