package com.practice.placetracker.model.location_api;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationClient {
    private FusedLocationProviderClient timeLocationProvider;
    private FusedLocationProviderClient distanceLocationProvider;
    private LocationRequest timeLocationRequest;
    private LocationRequest distanceLocationRequest;


    private final long updateTime;
    private final float updateDistance;

    public LocationClient(Context context, long updateTime, float updateDistance) {
        Log.i("MyLog", "LocationClient - in constructor");
        this.updateTime = updateTime;
        this.updateDistance = updateDistance;
        timeLocationProvider = LocationServices.getFusedLocationProviderClient(context);
        distanceLocationProvider = LocationServices.getFusedLocationProviderClient(context);
    }

    private void createLocationRequests(){
        Log.i("MyLog", "LocationClient - in createLocationRequests()");
        timeLocationRequest = LocationRequest.create();
        timeLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        timeLocationRequest.setInterval(updateTime);
        timeLocationRequest.setFastestInterval(updateTime);

        distanceLocationRequest = LocationRequest.create();
        distanceLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        distanceLocationRequest.setSmallestDisplacement(updateDistance);
        distanceLocationRequest.setInterval(0);
        distanceLocationRequest.setFastestInterval(0);

    }

    public void startLocationUpdate(LocationCallback timeLocationCallback, LocationCallback distanceLocationCallback){
        Log.i("MyLog", "LocationClient - in startLocationUpdate()");
        createLocationRequests();
        timeLocationProvider.requestLocationUpdates(timeLocationRequest,
                timeLocationCallback,
                Looper.getMainLooper());
        distanceLocationProvider.requestLocationUpdates(distanceLocationRequest,
                distanceLocationCallback,
                Looper.getMainLooper());
    }

    public void stopLocationUpdate(LocationCallback timeLocationCallback, LocationCallback distanceLocationCallback){
        Log.i("MyLog", "LocationClient - in stopLocationUpdate()");
        timeLocationProvider.removeLocationUpdates(timeLocationCallback);
        distanceLocationProvider.removeLocationUpdates(distanceLocationCallback);
    }
}
