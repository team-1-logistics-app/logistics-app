package com.austria.logistics.constants;

public class Constants {
    public static final String LOCATION_INVALID_MESSAGE = "%s is not valid location, the supported locations are: Sydney, Melbourne, Adelaide, Alice Springs, Brisbane, Darwin, Perth";
    public static final String LOCATION_NOT_FOUND_MESSAGE = "%s is not in the route.";
    public static final String LOCATION_ADDED_MESSAGE = "%s is successfully added to route with id %d ";
    public static final String LOCATION_REMOVED_MESSAGE = "%s is successfully removed from route with id %d ";
    public static final String ROUTE_IS_EMPTY_MESSAGE = "Route with id %d doesn't contain any locations yet.";
    public static final String LOCATION_ROUTE_INVALID_MESSAGE = "Route with id %d doesn't contain path from %s to %s";

    public static final int[][] distances = {
            {   0,  877, 1376, 2762,  909, 3935, 4016 },
            { 877,    0,  725, 2255, 1765, 3752, 3509 },
            {1376,  725,    0, 1530, 1927, 3027, 2785 },
            {2762, 2255, 1530,    0, 2993, 1497, 2481 },
            { 909, 1765, 1927, 2993,    0, 3426, 4311 },
            {3935, 3752, 3027, 1497, 3426,    0, 4025 },
            {4016, 3509, 2785, 2481, 4311, 4025,    0 }
    };

    private Constants(){};
}
