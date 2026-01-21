package com.austria.logistics.utils;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.exceptions.InvalidLocation;
import com.austria.logistics.models.enums.Cities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Parsers {
    private Parsers() {};

    public static Cities parseLocation(String location) {
        switch (location) {
            case "Sydney":
                return Cities.getCityValue("Sydney");
            case "Melbourne":
                return Cities.getCityValue("Melbourne");
            case "Adelaide":
                return Cities.getCityValue("Adelaide");
            case "Alice Springs":
                return Cities.getCityValue("Alice Springs");
            case "Brisbane":
                return Cities.getCityValue("Brisbane");
            case "Darwin":
                return Cities.getCityValue("Darwin");
            case "Perth":
                return Cities.getCityValue("Perth");
            default:
                throw new InvalidLocation(String.format(Constants.LOCATION_INVALID_MESSAGE, location));
        }
    }
    public static LocalDateTime parseEventTime(String eventTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d HH:mm", Locale.ENGLISH);
        return LocalDateTime.parse(eventTime,formatter);
    }
}
