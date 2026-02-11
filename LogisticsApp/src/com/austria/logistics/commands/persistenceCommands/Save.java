package com.austria.logistics.commands.persistenceCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.core.contracts.Repository;


import java.util.List;

public class Save extends BaseCommand {

    public Save(Repository repository) {
        super(repository);
    }

    @Override
    protected String executeCommand(List<String> parameters) {
        return getRepository().saveToFile();
    }

    @Override
    protected boolean requiresLogin() {
        return false;
    }

}
