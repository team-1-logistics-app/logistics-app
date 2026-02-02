package com.austria.logistics.utils;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.exceptions.InvalidValueException;

import java.util.List;

public class Validators {
    private Validators(){}


    public static void validateArgumentsCount(List<String> list, int expectedArgumentsCount) {
        if (list.size() < expectedArgumentsCount || list.size() > expectedArgumentsCount) {
            throw new IllegalArgumentException(String.format(Constants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE, expectedArgumentsCount, list.size()));
        }
    }

    public static void validateWeight(int weight){;
        if(weight <= 0){
            throw new InvalidValueException(Constants.WEIGHT_VALUE_INVALID_MESSAGE);
        }
    }
}
