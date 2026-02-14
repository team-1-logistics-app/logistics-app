package com.austria.logistics.models;

import com.austria.logistics.models.enums.CityName;
import com.austria.logistics.models.contracts.Location;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocationImpl implements Location {
    private CityName location;
    private LocalDateTime eventTime;

    public LocationImpl(CityName location){
        this.setLocation(location);
    }

    public LocationImpl(CityName location, LocalDateTime eventTime){
        this.setLocation(location);
        this.setEventTime(eventTime);
    }

    @Override
    public void setLocation(CityName location) {
        this.location = location;
    }

    @Override
    public CityName getLocation() {
        return this.location;
    }

    @Override
    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public LocalDateTime getEventTime() {
        return this.eventTime;
    }

    @Override
    public String getEventTimeAsString() {
        LocalDateTime time = this.getEventTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d HH:mm", Locale.ENGLISH);
        return time.format(formatter);
    }
}
