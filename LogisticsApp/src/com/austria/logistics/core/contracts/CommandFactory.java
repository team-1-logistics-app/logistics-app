package com.austria.logistics.core.contracts;

import com.austria.logistics.commands.contracts.Command;

public interface CommandFactory {
    Command createCommandFromCommandName(String commandTypeAsString, Repository repository);
}
