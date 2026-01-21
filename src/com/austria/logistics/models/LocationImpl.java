package com.austria.logistics.models;

import com.austria.logistics.models.enums.Cities;
import com.austria.logistics.models.contracts.Location;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocationImpl implements Location {
    private Cities location;
    private LocalDateTime eventTime;

    public LocationImpl(Cities location, LocalDateTime eventTime){
        this.setLocation(location);
        this.setEventTime(eventTime);
    };


    @Override
    public void setLocation(Cities location) {
        this.location = location;
    }

    @Override
    public Cities getLocation() {
        return this.location;
    }

    @Override
    public String getLocationAsString() {
        return location.getCityName();
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
