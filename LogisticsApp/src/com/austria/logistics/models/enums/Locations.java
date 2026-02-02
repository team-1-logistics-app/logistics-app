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

    private final String cityName;

    Locations(String city){
        this.cityName = city;
    }

    public String getCityName(){
        return cityName;
    }

    public static Locations getCityValue(String cityName){
        for(Locations city: values()){
            if(city.cityName.equalsIgnoreCase(cityName)){
                return city;
            }
        }
        throw new InvalidLocationException(String.format(Constants.LOCATION_INVALID_MESSAGE,cityName));
    }
}
