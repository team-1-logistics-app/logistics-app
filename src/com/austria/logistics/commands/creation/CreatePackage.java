package com.austria.logistics.commands.creation;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.InvalidLocationException;
import com.austria.logistics.exceptions.InvalidWeightValueException;
import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.utils.Parsers;
import com.austria.logistics.utils.Validators;

import javax.swing.plaf.PanelUI;
import java.util.List;

public class CreatePackage implements Command {
    public static final int EXPECTED_NUMBER_OF_ARGUMENTS = 4;
    private final Repository repository;

    public CreatePackage() {
        this.repository = new RepositoryImpl();
    }


    @Override
    public String execute(List<String> parameters) {
        Locations startLocation;
        Locations endLocation;
        int weight;
        String contactInfo;

        try {
            Validators.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);
            startLocation = Parsers.parseLocation(parameters.get(0));
            endLocation = Parsers.parseLocation(parameters.get(1));
            weight = Parsers.parseWeight(parameters.get(2));
            Validators.validateWeight(weight);
        } catch (IllegalArgumentException | InvalidLocationException | InvalidWeightValueException e) {
            return e.getMessage();
        }
        contactInfo = parameters.get(3);

        return createPackage(startLocation, endLocation, weight, contactInfo);
    }

    public String createPackage(Locations startLocation, Locations endLocation, int weight, String contactInfo) {
        int id = this.repository.createPackage(startLocation, endLocation, weight, contactInfo).getId();

        return String.format(Constants.PACKAGE_CREATED, id);
    }
}
