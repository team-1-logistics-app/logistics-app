package com.austria.logistics.core;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.CommandFactory;
import com.austria.logistics.core.contracts.Engine;
import com.austria.logistics.core.contracts.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EngineImpl implements Engine {
    private final CommandFactory commandFactory;
    private final Repository repository;

    public EngineImpl(){
        this.repository = new RepositoryImpl();
        this.commandFactory = new CommandFactoryImpl();
    }

    @Override
    public void start() {
        Scanner input = new Scanner(System.in);

        while (true){
            String inputLine = input.nextLine();
            if(inputLine.isEmpty()){
                continue;
            }
            if(inputLine.equalsIgnoreCase(Constants.TERMINATION_COMMAND)){
                break;
            }
            processCommand(inputLine);
        }
    }

    private void print(String output){
        System.out.println(output.trim());
        System.out.println(Constants.REPORT_SEPARATOR);
    }

    private void processCommand(String inputLine){
        String commandName = extractCommandName(inputLine);
        List<String> parameters = extractParameters(inputLine);
        Command command;
        String execResult;
        try {
            command = commandFactory.createCommandFromCommandName(commandName,repository);
            execResult = command.execute(parameters);
        }catch (IllegalArgumentException e){
            execResult = e.getMessage();
        }
        print(execResult);
    }

    private String extractCommandName(String inputLine){
        return inputLine.split(" ")[0];
    }

    private List<String> extractParameters(String inputLine){
        String[] parts = inputLine.split(" ");

        List<String> parameters = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            parameters.add(parts[i]);
        }
        return parameters;
    }
}

