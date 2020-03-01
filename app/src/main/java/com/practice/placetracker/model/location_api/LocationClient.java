package com.practice.placetracker.model.location_api;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.practice.placetracker.android_utils.ILog;
import com.practice.placetracker.android_utils.Logger;

public class LocationClient implements ILocationClient{

    private final ILog logger = Logger.withTag("MyLog");

    private FusedLocationProviderClient timeLocationProvider;
    private FusedLocationProviderClient distanceLocationProvider;
    private LocationRequest timeLocationRequest;
    private LocationRequest distanceLocationRequest;

    public LocationClient(Context context, long updateTime, float updateDistance) {
        logger.log("LocationClient in constructor");
        timeLocationProvider = LocationServices.getFusedLocationProviderClient(context);
        distanceLocationProvider = LocationServices.getFusedLocationProviderClient(context);
        createLocationRequestByTime(updateTime);
        createLocationRequestByDistance(updateDistance);
    }

    public void createLocationRequestByTime(long updateTime){
        timeLocationRequest = LocationRequest.create();
        logger.log("LocationClient in createLocationRequestByTime()");
        timeLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        timeLocationRequest.setInterval(updateTime);
        timeLocationRequest.setFastestInterval(updateTime);
    }

    public void createLocationRequestByDistance(float updateDistance){
        distanceLocationRequest = LocationRequest.create();
        logger.log("LocationClient in createLocationRequestByDistance()");
        distanceLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        distanceLocationRequest.setSmallestDisplacement(updateDistance);
        distanceLocationRequest.setInterval(0);
        distanceLocationRequest.setFastestInterval(0);
    }

    public void startLocationUpdateByTime(LocationCallback timeLocationCallback){
        logger.log("LocationClient in startLocationUpdateByTime()");
        timeLocationProvider.requestLocationUpdates(timeLocationRequest,
                timeLocationCallback,
                Looper.getMainLooper());
    }

    public void startLocationUpdateByDistance(LocationCallback distanceLocationCallback){
        logger.log("LocationClient in startLocationUpdateByDistance()");
        distanceLocationProvider.requestLocationUpdates(distanceLocationRequest,
                distanceLocationCallback,
                Looper.getMainLooper());
    }

    public void stopLocationUpdateByTime(LocationCallback timeLocationCallback){
        logger.log("LocationClient in stopLocationUpdateByTime()");
        timeLocationProvider.removeLocationUpdates(timeLocationCallback);
    }

    public void stopLocationUpdateByDistance(LocationCallback distanceLocationCallback){
        logger.log("LocationClient in stopLocationUpdateByDistance()");
        distanceLocationProvider.removeLocationUpdates(distanceLocationCallback);
    }
}
