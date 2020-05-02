package com.practice.placetracker.model.tracker;

import android.location.Location;

public class TrackingResult {
    private final String type;
    private final Location location;

    public TrackingResult(String type, Location location) {
        this.type = type;
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }
}

