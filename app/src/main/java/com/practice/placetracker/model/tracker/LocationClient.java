package com.practice.placetracker.model.tracker;

import android.content.Context;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.model.cache.SessionCache;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class LocationClient implements LocationsSupplier {

    private final ILog logger;

    private final FusedLocationProviderClient timeLocationProvider;
    private final FusedLocationProviderClient distanceLocationProvider;
    private final PublishSubject<TrackingResult> locationsSubject = PublishSubject.create();
    private LocationRequest distanceLocationRequest;
    private LocationRequest timeLocationRequest;
    private final LocationCallback timeLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            locationsSubject.onNext(new TrackingResult(SessionCache.TYPE_TIME, locationResult.getLastLocation()));
        }
    };
    private final LocationCallback distanceLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            locationsSubject.onNext(new TrackingResult(SessionCache.TYPE_DISTANCE, locationResult.getLastLocation()));
        }
    };

    public LocationClient(Context context, long updateTime, float updateDistance, ILog logger) {
        this.logger = logger;
        logger.log("LocationClient in constructor");
        timeLocationProvider = LocationServices.getFusedLocationProviderClient(context);
        distanceLocationProvider = LocationServices.getFusedLocationProviderClient(context);

        timeLocationRequest = LocationRequest.create();
        logger.log("LocationClient in createLocationRequestByTime()");
        timeLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        timeLocationRequest.setInterval(updateTime);
        timeLocationRequest.setFastestInterval(updateTime);

        distanceLocationRequest = LocationRequest.create();
        logger.log("LocationClient in createLocationRequestByDistance()");
        distanceLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        distanceLocationRequest.setSmallestDisplacement(updateDistance);
        distanceLocationRequest.setInterval(0);
        distanceLocationRequest.setFastestInterval(0);
    }

    @Override
    public void startLocationObserving() {
        logger.log("LocationClient in startLocationUpdateByTime()");
        timeLocationProvider.requestLocationUpdates(timeLocationRequest,
                timeLocationCallback, Looper.getMainLooper());

        logger.log("LocationClient in startLocationUpdateByDistance()");
        distanceLocationProvider.requestLocationUpdates(distanceLocationRequest,
                distanceLocationCallback, Looper.getMainLooper());
    }

    @Override
    public void stopLocationObserving() {
        logger.log("LocationClient in stopLocationUpdateByTime()");
        timeLocationProvider.removeLocationUpdates(timeLocationCallback);

        logger.log("LocationClient in stopLocationUpdateByDistance()");
        distanceLocationProvider.removeLocationUpdates(distanceLocationCallback);
    }

    public Observable<TrackingResult> getLocationsObservable() {
        return locationsSubject;
    }
}

