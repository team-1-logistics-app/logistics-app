package com.austria.logistics.models;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.models.enums.CityName;


public class Distance {
    private Distance(){}

    public static int calculateDistance(CityName from, CityName to){
        return Constants.distances[from.ordinal()][to.ordinal()];
    }
}
