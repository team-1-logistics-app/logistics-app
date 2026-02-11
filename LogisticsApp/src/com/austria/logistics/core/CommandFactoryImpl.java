package com.austria.logistics.core;

import com.austria.logistics.commands.assignCommands.AssignLocation;
import com.austria.logistics.commands.assignCommands.AssignPackage;
import com.austria.logistics.commands.assignCommands.AssignTruck;
import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.commands.creationCommands.CreatePackage;
import com.austria.logistics.commands.creationCommands.CreateRoute;
import com.austria.logistics.commands.enums.CommandType;
import com.austria.logistics.commands.persistenceCommands.Save;
import com.austria.logistics.commands.showCommands.ShowPackage;
import com.austria.logistics.commands.showCommands.ShowPackages;
import com.austria.logistics.commands.showCommands.ShowRoute;
import com.austria.logistics.commands.showCommands.ShowTrucks;
import com.austria.logistics.commands.userCommands.Login;
import com.austria.logistics.commands.userCommands.Logout;
import com.austria.logistics.commands.userCommands.Register;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.CommandFactory;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.utils.Parsers;

public class CommandFactoryImpl implements CommandFactory {
    ;

    @Override
    public Command createCommandFromCommandName(String commandTypeAsString, Repository repository) {
        CommandType commandType = Parsers.tryParseEnum(commandTypeAsString, CommandType.class, String.format(Constants.INVALID_COMMAND,commandTypeAsString));

        switch (commandType){
            case ASSIGNLOCATION:
                return new AssignLocation(repository);
            case ASSIGNPACKAGE:
                return new AssignPackage(repository);
            case ASSIGNTRUCK:
                return new AssignTruck(repository);
            case CREATEPACKAGE:
                return new CreatePackage(repository);
            case CREATEROUTE:
                return new CreateRoute(repository);
            case SHOWPACKAGES:
                return new ShowPackages(repository);
            case SHOWPACKAGE:
                return new ShowPackage(repository);
            case SHOWROUTE:
                return new ShowRoute(repository);
            case SHOWTRUCKS:
                return new ShowTrucks(repository);
            case LOGIN:
                return new Login(repository);
            case LOGOUT:
                return new Logout(repository);
            case REGISTER:
                return new Register(repository);
            case SAVE:
                return new Save(repository);
            default:
                throw new IllegalArgumentException(String.format(Constants.INVALID_COMMAND, commandTypeAsString));
        }
    }
}
