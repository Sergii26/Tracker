package com.practice.placetracker.model.cache;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class SessionCacheImpl implements SessionCache {

    private static final SessionCacheImpl session = new SessionCacheImpl();

    private final BehaviorSubject<Boolean> trackingState = BehaviorSubject.createDefault(false);
    private final BehaviorSubject<Integer> locationsCount = BehaviorSubject.createDefault(0);
    private int locationsByTime;
    private int locationsByDistance;
    private long startTime = 0;

    public static SessionCacheImpl getInstance() {
        return session;
    }

    @Override
    public void updateSession(String indication) {
        if (TYPE_TIME.equals(indication)) {
            locationsByTime++;
        }
        if (TYPE_DISTANCE.equals(indication)) {
            locationsByDistance++;
        }
        locationsCount.onNext(locationsCount.getValue() + 1);
    }

    @Override
    public void putStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public void drop() {
        locationsCount.onNext(0);
        locationsByTime = 0;
        locationsByDistance = 0;
    }

    @Override
    public int getLocationCount() {
        return locationsCount.getValue();
    }

    @Override
    public int getLocationsByTime() {
        return locationsByTime;
    }

    @Override
    public void setIsTracking(boolean isTracking) {
        trackingState.onNext(isTracking);
    }

    @Override
    public Observable<Boolean> getTrackingStateObservable() {
        return trackingState;
    }

    @Override
    public Observable<Integer> getLocationsCountObservable() {
        return locationsCount;
    }

    @Override
    public int getLocationsByDistance() {
        return locationsByDistance;
    }

}

