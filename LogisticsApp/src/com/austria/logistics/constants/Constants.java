package com.austria.logistics.constants;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
    public static final String LOCATION_INVALID_MESSAGE = "%s is not valid location, the supported locations are: Sydney, Melbourne, Adelaide, Alice Springs, Brisbane, Darwin, Perth.";
    public static final String LOCATION_NOT_FOUND_MESSAGE = "%s is not in the route.";
    public static final String LOCATION_ADDED_MESSAGE = "%s is successfully added to route with id %d .";
    public static final String LOCATION_REMOVED_MESSAGE = "%s is successfully removed from route with id %d .";
    public static final String LOCATION_ROUTE_INVALID_MESSAGE = "Route with id %d doesn't contain path from %s to %s.";
    public static final String LOCATION_PREVIOUS_IS_SAME_MESSAGE = "Route with id %d already has %s as last stop.";

    public static final String ROUTE_IS_EMPTY_MESSAGE = "Route with id %d doesn't contain any locations yet.";
    public static final String ROUTE_IS_EMPTY_WHILE_ADDING_AS_FIRST_LOCATION_MESSAGE = "Route with id %d doesn't contain any locations yet, you should provide departure time for your start location.";
    public static final String ROUTE_IS_NOT_EMPTY_MESSAGE = "Route with id %d already contains location with depart time(starting location).";
    public static final String ROUTE_NOT_ENOUGH_LOCATIONS_MESSAGE = "Route with id %d doesn't contain enough locations to calculate schedule. ";
    public static final String ROUTE_CREATED_MESSAGE = "Route with id %d was created!";
    public static final String ROUTE_ASSIGN_ERROR_MESSAGE = "Route with id %d doesn't have enough locations assigned yet, please assign at least 2 locations before assigning truck to it.";

    public static final String INVALID_WEIGHT_VALUE_MESSAGE = "Weight can't be 0 or less kg.";
    public static final String INVALID_VALUE_FORMAT_MESSAGE = "%s has to be valid integer.";
    public static final String INVALID_ENUM_VALUE_FORMAT_MESSAGE = "%s has to be valid enum value.";
    public static final String INVALID_NUMBER_OF_ARGUMENTS_MESSAGE = "Invalid number of arguments. Expected: %d, Received: %d.";
    public static final String INVALID_TIME_FORMAT_MESSAGE = "Invalid event time format. Expected format: MMM d HH:mm.";
    public static final String INVALID_TIME_IS_PAST_MESSAGE = "The date-time is in the past, current date-time is %s.";
    public static final String INVALID_COMMAND = "Invalid command name: %s!";

    public static final String PACKAGE_CREATED_MESSAGE = "Package with id %d was created!";
    public static final String PACKAGE_ASSIGNED_MESSAGE = "Package with id %d was assigned to truck %s with id %d!";
    public static final String PACKAGE_ASSIGN_ERROR_MESSAGE = "Package with id %d cannot be assigned to route with id %d because the route doesn't contain path from %s to %s.";
    public static final String PACKAGE_ALREADY_ASSIGNED_ERROR_MESSAGE = "Package with id %d is already assigned to truck %s with id %d";

    public static final String TRUCK_INVALID_TYPE_MESSAGE = "%s is unsupported truck type, use one of those: Scania, Man or Actros";
    public static final String TRUCK_TYPE_NOT_AVAILABLE_MESSAGE = "All trucks %s are busy at the moment, try different truck type.";
    public static final String TRUCK_ASSIGNED_MESSAGE = "Truck %s with id %d was assigned to route with id %d!";
    public static final String TRUCK_MAXCAPACITY_REACHED_MESSAGE = "Truck %s with id %d has reached the max load capacity, please select another truck!";
    public static final String TRUCK_NOT_ASSIGNED_MESSAGE = "Truck %s with id %d is not assigned to route yet, assign it to route before assigning packages to it.";
    public static final double TRUCK_AVERAGE_SPEED_KMH = 87;

    public final static String USER_NOT_LOGGED = "You are not logged in! Please login first!";
    public final static String USER_NOT_FOUND = "Cannot find user with username %s";
    public final static String USER_PASSWORD_MISMATCH = "Incorrect username or password!";
    public final static String USER_LOGGED_IN = "User %s successfully logged in!";
    public final static String USER_LOGGED_OUT = "You logged out!";
    public final static String USER_LOGGED_IN_ALREADY = "User %s is logged in! Please log out first!";
    public final static String USER_REGISTERED = "User %s registered successfully!";
    public final static String USER_ALREADY_EXIST = "User %s already exist. Choose a different username!";
    public final static String USER_NOT_EMPLOYEE = "You are not logged in as employee!";

    public final static String STATE_DIR_TO_SAVE = "saves";
    public final static String STATE_FILE_TO_SAVE_PACKAGES = "packages.txt";
    public final static String STATE_FILE_TO_SAVE_TRUCKS = "trucks.txt";
    public final static String STATE_FILE_TO_SAVE_ROUTES = "routes.txt";
    public final static String STATE_FILE_TO_SAVE_USERS = "users.txt";

    public final static String STATE_SAVED_TO_FILE = "State successfully saved to file!";
    public final static String STATE_FAILED_TO_SAVE = "State failed to save to file!";
    public final static String STATE_FAILED_TO_LOAD = "State failed to load from file!";
    public final static String STATE_LOADED_FROM_FILE = "Loaded state from file!";

    public static final String ELEMENT_NOT_FOUND_MESSAGE = "No record with id %d in the repository";


    public final static Path FILE_PATH_PACKAGES = Paths.get(Constants.STATE_DIR_TO_SAVE, Constants.STATE_FILE_TO_SAVE_PACKAGES);
    public final static Path FILE_PATH_TRUCKS = Paths.get(Constants.STATE_DIR_TO_SAVE, Constants.STATE_FILE_TO_SAVE_TRUCKS);
    public final static Path FILE_PATH_ROUTES = Paths.get(Constants.STATE_DIR_TO_SAVE, Constants.STATE_FILE_TO_SAVE_ROUTES);
    public final static Path FILE_PATH_USERS = Paths.get(Constants.STATE_DIR_TO_SAVE, Constants.STATE_FILE_TO_SAVE_USERS);


    public static final String REPORT_SEPARATOR = "####################";
    public static final String EMPTY_COMMAND_ERROR = "Command cannot be empty.";
    public static final String TERMINATION_COMMAND = "Exit";

    public static final int[][] distances = {
            {0, 877, 1376, 2762, 909, 3935, 4016},
            {877, 0, 725, 2255, 1765, 3752, 3509},
            {1376, 725, 0, 1530, 1927, 3027, 2785},
            {2762, 2255, 1530, 0, 2993, 1497, 2481},
            {909, 1765, 1927, 2993, 0, 3426, 4311},
            {3935, 3752, 3027, 1497, 3426, 0, 4025},
            {4016, 3509, 2785, 2481, 4311, 4025, 0}
    };

    private Constants() {
    }

    ;
}
