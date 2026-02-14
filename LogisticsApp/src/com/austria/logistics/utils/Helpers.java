package com.austria.logistics.utils;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.exceptions.FailedToLoadFromFileException;
import com.austria.logistics.models.contracts.Savealbe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

public class Helpers {
    private Helpers() {
    }

    public static <T extends Savealbe> void readFileLines(Path pathOfTheFile, List<T> loadToList, Function<String, T> objectFromString) {
        loadToList.clear();
        try {
            List<String> lines = Files.readAllLines(pathOfTheFile);
            lines.stream().map(String::trim).filter(line -> !line.isEmpty()).forEach(line -> loadToList.add(objectFromString.apply(line)));
        } catch (IOException e) {
            throw new FailedToLoadFromFileException(Constants.STATE_FAILED_TO_LOAD);
        }
    }
}
