package com.austria.logistics.utils;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.exceptions.InvalidLocationException;
import com.austria.logistics.exceptions.InvalidTimeFormatException;
import com.austria.logistics.exceptions.InvalidTruckTypeException;
import com.austria.logistics.exceptions.InvalidValueException;
import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.models.enums.TruckType;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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

    public static <E extends Enum<E>> E tryParseEnum(String valueToParse, Class<E> type, String errorMessage) {
        try {
            return Enum.valueOf(type, valueToParse.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(errorMessage, valueToParse));
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

    public static int parseToInteger(String elementType, String value) {
        int valueInt;
        try {
            valueInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new InvalidValueException(String.format(Constants.INVALID_VALUE_FORMAT_MESSAGE, elementType));
        }
        return valueInt;
    }

    public static TruckType parseTruck(String truck) {
        return Arrays.stream(TruckType.values())
                .filter(truckType -> truckType.getDisplayName().equalsIgnoreCase(truck))
                .findFirst()
                .orElseThrow(() -> new InvalidTruckTypeException(String.format(Constants.TRUCK_INVALID_TYPE_MESSAGE, truck)));
    }
}
