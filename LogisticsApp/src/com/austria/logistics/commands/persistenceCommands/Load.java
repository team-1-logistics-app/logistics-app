package com.austria.logistics.commands.persistenceCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.core.contracts.Repository;


import java.util.List;

public class Load extends BaseCommand {
    public Load(Repository repository) {
        super(repository);
    }
    //NO ARGUMENTS ARE EXPECTED
    @Override
    protected String executeCommand(List<String> parameters) {
        return getRepository().loadFromFile();
    }

    @Override
    protected boolean requiresLogin() {
        return false;
    }
}
