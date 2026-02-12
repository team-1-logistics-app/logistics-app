package com.austria.logistics.commands.unassignCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.core.contracts.Repository;

import java.util.List;

public class UnassignPackage extends BaseCommand {
    public UnassignPackage(Repository repository) {
        super(repository);
    }



    @Override
    protected String executeCommand(List<String> parameters) {
        return "";
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }
}
