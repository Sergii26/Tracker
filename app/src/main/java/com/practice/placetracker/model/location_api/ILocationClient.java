package com.practice.placetracker.model.location_api;

import com.google.android.gms.location.LocationCallback;

public interface ILocationClient {
    void createLocationRequestByTime(long updateTime);
    void createLocationRequestByDistance(float updateDistance);
    void startLocationUpdateByTime(LocationCallback timeLocationCallback);
    void startLocationUpdateByDistance(LocationCallback distanceLocationCallback);
    void stopLocationUpdateByTime(LocationCallback timeLocationCallback);
    void stopLocationUpdateByDistance(LocationCallback distanceLocationCallback);
}
