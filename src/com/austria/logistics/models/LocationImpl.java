package com.austria.logistics.models;

import com.austria.logistics.models.enums.Locations;
import com.austria.logistics.models.contracts.Location;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocationImpl implements Location {
    private Locations location;
    private LocalDateTime eventTime;

    public LocationImpl(Locations location){
        this.setLocation(location);
    }


    public LocationImpl(Locations location, LocalDateTime eventTime){
        this.setLocation(location);
        this.setEventTime(eventTime);
    }


    @Override
    public void setLocation(Locations location) {
        this.location = location;
    }

    @Override
    public Locations getLocation() {
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
