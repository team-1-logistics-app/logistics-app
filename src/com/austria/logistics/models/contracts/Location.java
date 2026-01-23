package com.austria.logistics.models.contracts;

import com.austria.logistics.models.enums.Locations;

import java.time.LocalDateTime;

public interface Location {
    void setLocation(Locations location);
    Locations getLocation();

    void setEventTime(LocalDateTime eventTime);
    LocalDateTime getEventTime();
    String getEventTimeAsString();
}
