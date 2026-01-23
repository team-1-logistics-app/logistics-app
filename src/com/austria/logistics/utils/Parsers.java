package com.austria.logistics.utils;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.exceptions.InvalidLocationException;
import com.austria.logistics.models.enums.Locations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Parsers {
    private Parsers() {}

    public static Locations parseLocation(String location) {
        switch (location) {
            case "Sydney":
                return Locations.getCityValue("Sydney");
            case "Melbourne":
                return Locations.getCityValue("Melbourne");
            case "Adelaide":
                return Locations.getCityValue("Adelaide");
            case "Alice Springs":
                return Locations.getCityValue("Alice Springs");
            case "Brisbane":
                return Locations.getCityValue("Brisbane");
            case "Darwin":
                return Locations.getCityValue("Darwin");
            case "Perth":
                return Locations.getCityValue("Perth");
            default:
                throw new InvalidLocationException(String.format(Constants.LOCATION_INVALID_MESSAGE, location));
        }
    }
    public static LocalDateTime parseEventTime(String eventTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d HH:mm", Locale.ENGLISH);
        return LocalDateTime.parse(eventTime,formatter);
    }
}
