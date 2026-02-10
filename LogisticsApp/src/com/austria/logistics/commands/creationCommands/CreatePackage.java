package com.austria.logistics.commands.creationCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.InvalidLocationException;
import com.austria.logistics.exceptions.InvalidValueException;
import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.utils.Parsers;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class CreatePackage extends BaseCommand {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 4;

    public CreatePackage(Repository repository) {
        super(repository);
    }


    //EXPECTED ARGUMENTS ARE STRING STARTLOCATION, STRING ENDLOCATION, STRING WEIGHT, STRING CONTACTINFO
    @Override
    public String executeCommand(List<String> parameters) {
        Locations startLocation;
        Locations endLocation;
        int weight;
        String contactInfo;

        try {
            Validators.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);
            startLocation = Parsers.parseLocation(parameters.get(0));
            endLocation = Parsers.parseLocation(parameters.get(1));
            weight = Parsers.parseToInteger("Weight", parameters.get(2));
            Validators.validateWeight(weight);
        } catch (IllegalArgumentException | InvalidLocationException | InvalidValueException e) {
            return e.getMessage();
        }
        contactInfo = parameters.get(3);

        return createPackage(startLocation, endLocation, weight, contactInfo);
    }

    private String createPackage(Locations startLocation, Locations endLocation, int weight, String contactInfo) {
        int id = getRepository().createPackage(startLocation, endLocation, weight, contactInfo).getId();

        return String.format(Constants.PACKAGE_CREATED_MESSAGE, id);
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }
}
