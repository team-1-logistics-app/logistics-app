package com.austria.logistics.commands;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.NotLoggedInException;


import java.util.List;

public abstract class BaseCommand implements Command {
    private  final Repository repository;
    protected BaseCommand(Repository repository){ this.repository = repository;}

    @Override
    public String execute(List<String> parameters){
        if(requiresLogin() && !this.repository.hasLoggedUser()){
            throw new NotLoggedInException(Constants.USER_NOT_LOGGED);
        }
        return executeCommand(parameters);
    }

    protected Repository getRepository(){
      return this.repository;
    }

    protected abstract boolean requiresLogin();
    protected abstract String executeCommand(List<String> parameters);
}
