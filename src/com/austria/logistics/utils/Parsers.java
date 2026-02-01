package com.austria.logistics.utils;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.exceptions.InvalidLocationException;
import com.austria.logistics.exceptions.InvalidTimeFormatException;
import com.austria.logistics.exceptions.InvalidWeightValueException;
import com.austria.logistics.models.enums.Locations;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Parsers {
    private Parsers() {
    }

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

    public static LocalDateTime parseEventTime(String eventTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm", Locale.ENGLISH);
        LocalDateTime time;

        try {
            time = LocalDateTime.parse(eventTime, formatter);
        } catch (DateTimeException e) {
            throw new InvalidTimeFormatException(Constants.INVALID_TIME_FORMAT_MESSAGE);
        }

        LocalDateTime now = LocalDateTime.now();

        if (time.isBefore(now)) {
            throw new InvalidTimeFormatException(String.format(Constants.INVALID_TIME_IS_PAST_MESSAGE, now.format(formatter)));
        }

        return time;
    }

    public static int parseWeight(String weight){
        int weightInt;
        try {
            weightInt = Integer.parseInt(weight);
        }catch (NumberFormatException e){
            throw new InvalidWeightValueException(Constants.WEIGHT_VALUE_INVALID_FORMAT_MESSAGE);
        }
        return weightInt;
    }
}
