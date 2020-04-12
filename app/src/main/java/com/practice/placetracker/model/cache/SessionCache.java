package com.practice.placetracker.model.cache;

import io.reactivex.Observable;

public interface SessionCache {
    String TYPE_TIME = "time";
    String TYPE_DISTANCE = "distance";

    void drop();

    int getLocationCount();

    int getLocationsByDistance();

    int getLocationsByTime();

    void setIsTracking(boolean isTracking);

    Observable<Boolean> getTrackingStateObservable();

    Observable<Integer> getLocationsCountObservable();

    void updateSession(String indication);

    void putStartTime(long currentTimeMillis);

    long getStartTime();
}

