package com.austria.logistics.commands.userCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.core.contracts.Repository;

import java.util.List;

public class ReadMail extends BaseCommand {
    public ReadMail(Repository repository) {
        super(repository);
    }
    //NO ARGUMENTS ARE EXPECTED
    @Override
    protected String executeCommand(List<String> parameters) {
        return getRepository().getLoggedUser().printMailBox();
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }
}
