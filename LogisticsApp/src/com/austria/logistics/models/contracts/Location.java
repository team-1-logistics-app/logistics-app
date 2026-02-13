package com.austria.logistics.models.contracts;

import com.austria.logistics.models.enums.CityName;

import java.time.LocalDateTime;

public interface Location {
    void setLocation(CityName location);
    CityName getLocation();

    void setEventTime(LocalDateTime eventTime);
    LocalDateTime getEventTime();
    String getEventTimeAsString();
}
