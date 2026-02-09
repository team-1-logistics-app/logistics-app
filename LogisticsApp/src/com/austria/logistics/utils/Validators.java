package com.austria.logistics.utils;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.exceptions.InvalidValueException;

import java.util.List;

public class Validators {
    private Validators(){}


    public static void validateArgumentsCount(List<String> list, int expectedArgumentsCount) {
        if (list.size() < expectedArgumentsCount) {
            throw new IllegalArgumentException(String.format(Constants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE, expectedArgumentsCount, list.size()));
        }
    }

    public static void validateIntegerRange(int value,int min, int max, String errorMessage){
        if(value < min || value > max){
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void validateStringLenght(String value, int min,int max, String message) {
        if(value.length() < min || value.length() > max){
            throw new IllegalArgumentException(message);
        }
    }

    public static void validateWeight(int weight){;
        if(weight <= 0){
            throw new InvalidValueException(Constants.INVALID_WEIGHT_VALUE_MESSAGE);
        }
    }
}
