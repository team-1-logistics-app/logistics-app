package com.austria.logistics.models;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.models.enums.Locations;


public class Distance {
    private Distance(){}


    public static int calculateDistance(Locations from, Locations to){
        return Constants.distances[from.ordinal()][to.ordinal()];
    }
}
