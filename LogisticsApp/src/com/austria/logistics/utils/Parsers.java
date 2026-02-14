package com.austria.logistics.utils;

import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.InvalidLocationException;
import com.austria.logistics.exceptions.InvalidTimeFormatException;
import com.austria.logistics.exceptions.InvalidTruckTypeException;
import com.austria.logistics.exceptions.InvalidValueException;
import com.austria.logistics.models.LocationImpl;
import com.austria.logistics.models.PackageImpl;
import com.austria.logistics.models.RouteImpl;
import com.austria.logistics.models.UserImpl;
import com.austria.logistics.models.contracts.*;
import com.austria.logistics.models.contracts.Package;
import com.austria.logistics.models.enums.CityName;
import com.austria.logistics.models.enums.TruckType;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.models.vehicles.TruckImpl;
import com.austria.logistics.models.vehicles.contracts.Truck;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Parsers {
    private Parsers() {}

    public static CityName parseLocation(String location) {
        switch (location) {
            case "Sydney":
                return CityName.getCityValue("Sydney");
            case "Melbourne":
                return CityName.getCityValue("Melbourne");
            case "Adelaide":
                return CityName.getCityValue("Adelaide");
            case "Alice Springs":
                return CityName.getCityValue("Alice Springs");
            case "Brisbane":
                return CityName.getCityValue("Brisbane");
            case "Darwin":
                return CityName.getCityValue("Darwin");
            case "Perth":
                return CityName.getCityValue("Perth");
            default:
                throw new InvalidLocationException(String.format(Constants.LOCATION_INVALID_MESSAGE, location));
        }
    }

    public static <E extends Enum<E>> E tryParseEnum(String valueToParse, Class<E> type, String errorMessage) {
        try {
            return Enum.valueOf(type, valueToParse.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidValueException(String.format(errorMessage, valueToParse));
        }
    }

    public static LocalDateTime parseEventTimeToLocalDateTime(String value) {
        try {
            String cleaned = value.replace('\u00A0', ' ').trim();

            int year = LocalDate.now().getYear();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d HH:mm yyyy", Locale.ENGLISH);

            LocalDateTime parsedTime = LocalDateTime.parse(cleaned + " " + year, formatter);

            if (parsedTime.isBefore(LocalDateTime.now())) {
                throw new InvalidTimeFormatException(Constants.INVALID_TIME_IS_PAST_MESSAGE);
            }

            return parsedTime;

        } catch (DateTimeException e) {
            throw new InvalidTimeFormatException(Constants.INVALID_TIME_FORMAT_MESSAGE);
        }
    }

    public static String parseEventTimeToString(LocalDateTime eventTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d HH:mm", Locale.ENGLISH);
        return eventTime.format(formatter);
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

    public static <T extends Savealbe> List<String> parseCollectionToStringList(List<T> collectionToParse) {
        return collectionToParse.stream()
                .map(Savealbe::toSaveString)
                .toList();
    }

    public static Route routeFromSaveString(String line) {
        String[] elements = line.split("\\|");
        int id = Integer.parseInt(elements[0]);
        Route route = new RouteImpl(id);
        if (!elements[1].equals("NONE")) {
            List<String> LocationAndEventTimeList = List.of(elements[1].split(","));
            LocationAndEventTimeList.stream()
                    .map(element -> {
                        String[] parts = element.split("@");
                        CityName location = Parsers.parseLocation(parts[0]);
                        LocalDateTime eventTime = Parsers.parseEventTimeToLocalDateTime(parts[1]);
                        return (Location) new LocationImpl(location, eventTime);
                    }).forEach(route::addLocationFromLoad);
        }
        if (!elements[2].equals("NONE")) {
            route.setLoadedTruckId(Integer.parseInt(elements[2]));
        }
        return route;
    }

    public static Package packageFromSaveString(String line, Repository repository) {
        String[] elements = line.split("\\|");
        int id = Integer.parseInt(elements[0]);
        CityName startLocation = Parsers.parseLocation(elements[1]);
        CityName endLocation = Parsers.parseLocation(elements[2]);
        int weight = Integer.parseInt(elements[3]);
        String contactInfo = elements[4];

        Package pkg = new PackageImpl(id, startLocation, endLocation, weight, contactInfo);

        if(!elements[5].equals("NONE")){
            pkg.setEstimatedArrivalTime(Parsers.parseEventTimeToLocalDateTime(elements[5]));
        }

        if (!elements[6].equals("NONE")) {
            Truck assignedTruck = repository.findElementById(repository.getTrucks(), Integer.parseInt(elements[6]));
            pkg.setAssignedTruck(assignedTruck);
        }
        return pkg;
    }

    public static Truck truckFromSaveString(String line, Repository repository) {
        String[] elements = line.split("\\|");
        int id = Integer.parseInt(elements[0]);
        TruckType truckType = Parsers.parseTruck(elements[1]);

        Truck truck = new TruckImpl(id, truckType);

        if (!elements[2].equals("NONE")) {
            List<String> assignedPackagesIdStringList = List.of(elements[2].split(","));
            assignedPackagesIdStringList.stream().map(Integer::parseInt).forEach(truck::addAssignedPackageId);
        }
        boolean isAssigned = Boolean.parseBoolean(elements[3]);
        if (isAssigned) {
            truck.assign();
        } else {
            truck.unassign();
        }

        if (!elements[4].equals("NONE")) {
            Route route = repository.findElementById(repository.getRoutes(), Integer.parseInt(elements[4]));
            truck.setAssignedRoute(route);
        }

        int currentLoad = Integer.parseInt(elements[5]);
        truck.addLoad(currentLoad);

        return truck;
    }

    public static User userFromSaveString(String line, Repository repository){
        String[] elements = line.split("\\|");
        String username = elements[0];
        String firstName = elements[1];
        String lastName = elements[2];
        String password = elements[3];
        String email = elements[4];
        UserRole userRole = Parsers.tryParseEnum(elements[5],UserRole.class,String.format(Constants.INVALID_ENUM_VALUE_FORMAT_MESSAGE, elements[5]));
        User user = new UserImpl(username,firstName,lastName,password,email,userRole);
        if(!elements[6].equals("NONE")){
            List<String> mailbox = List.of(elements[6].split("\\^"));
            mailbox.forEach(user::receiveLetter);
        }
        return user;
    }
}
