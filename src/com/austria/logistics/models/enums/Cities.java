package com.austria.logistics.models.enums;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.exceptions.InvalidLocation;

public enum Cities {
    SYD("Sydney"),
    MEL("Melbourne"),
    ADL("Adelaide"),
    ASP("Alice Springs"),
    BRI("Brisbane"),
    DAR("Darwin"),
    PER("Perth");

    private final String cityName;

    Cities(String city){
        this.cityName = city;
    }

    public String getCityName(){
        return cityName;
    }

    public static Cities getCityValue(String cityName){
        for(Cities city: values()){
            if(city.cityName.equalsIgnoreCase(cityName)){
                return city;
            }
        }
        throw new InvalidLocation(String.format(Constants.LOCATION_INVALID_MESSAGE,cityName));
    }
}
