package com.austria.logistics.commands.contracts;

import java.util.List;

public interface Command {
    String execute(List<String> parameters);
}
