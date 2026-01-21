package com.austria.logistics.models.contracts;

import com.austria.logistics.models.enums.Cities;

import java.time.LocalDateTime;

public interface Location {
    void setLocation(Cities location);
    Cities getLocation();
    String getLocationAsString();

    void setEventTime(LocalDateTime eventTime);
    LocalDateTime getEventTime();
    String getEventTimeAsString();
}
