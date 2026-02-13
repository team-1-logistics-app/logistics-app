package com.austria.logistics.models.enums;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.exceptions.InvalidLocationException;

public enum CityName {
    SYD("Sydney"),
    MEL("Melbourne"),
    ADL("Adelaide"),
    ASP("Alice Springs"),
    BRI("Brisbane"),
    DAR("Darwin"),
    PER("Perth");

    private final String displayName;

    CityName(String city){
        this.displayName = city;
    }

    public String getDisplayName(){
        return displayName;
    }

    public static CityName getCityValue(String cityName){
        for(CityName city: values()){
            if(city.displayName.equalsIgnoreCase(cityName)){
                return city;
            }
        }
        throw new InvalidLocationException(String.format(Constants.LOCATION_INVALID_MESSAGE,cityName));
    }
}
