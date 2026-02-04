package com.austria.logistics.models.enums;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.exceptions.InvalidLocationException;

public enum Locations {
    SYD("Sydney"),
    MEL("Melbourne"),
    ADL("Adelaide"),
    ASP("Alice Springs"),
    BRI("Brisbane"),
    DAR("Darwin"),
    PER("Perth");

    private final String displayName;

    Locations(String city){
        this.displayName = city;
    }

    public String getDisplayName(){
        return displayName;
    }

    public static Locations getCityValue(String cityName){
        for(Locations city: values()){
            if(city.displayName.equalsIgnoreCase(cityName)){
                return city;
            }
        }
        throw new InvalidLocationException(String.format(Constants.LOCATION_INVALID_MESSAGE,cityName));
    }
}
