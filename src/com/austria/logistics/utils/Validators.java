package com.austria.logistics.utils;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.exceptions.InvalidWeightValueException;

public class Validators {
    private Validators(){}

    public static int validateWeight(int weight){
        if(weight <= 0){
            throw new InvalidWeightValueException(Constants.WEIGHT_VALUE_INVALID_MESSAGE);
        }
        return weight;
    }
}
