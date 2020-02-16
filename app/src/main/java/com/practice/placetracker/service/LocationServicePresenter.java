package com.practice.placetracker.service;

import android.util.Log;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.practice.placetracker.model.data.current_session.CurrentTrackingSession;
import com.practice.placetracker.model.data.room.DatabaseWorker;
import com.practice.placetracker.model.data.room.TrackedLocation;
import com.practice.placetracker.model.firestore_api.FirestoreClient;
import com.practice.placetracker.model.location_api.LocationClient;

public class LocationServicePresenter implements LocationServiceContract.BaseLocationServicePresenter {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static final long UPDATE_LOCATION_TIME = 1000 * 60 * 10;
    public static final float UPDATE_LOCATION_DISTANCE = 60;

    private LocationClient locationClient;
    private LocationCallback timeLocationCallback;
    private LocationCallback distanceLocationCallback;
    private FirestoreClient firestoreClient;
    private DatabaseWorker dbWorker;
    private CurrentTrackingSession currentSession;
    private String userEmail;
    private LocationServiceContract.BaseLocationService service;

    public LocationServicePresenter(LocationServiceContract.BaseLocationService service, LocationClient locationClient, DatabaseWorker dbWorker) {
        this.service = service;
        this.locationClient = locationClient;
        this.firestoreClient = new FirestoreClient();
        this.dbWorker = dbWorker;
        this.currentSession = CurrentTrackingSession.getInstance();
        dbWorker.deleteSentLocations(true);
        currentSession.clearCurrentSession();
        createLocationCallbacks();
    }

    private void createLocationCallbacks(){
        timeLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(final LocationResult locationResult) {
                Log.i("MyLog", "LocationServicePresenter - in onLocationResult() TIME");
                TrackedLocation location = new TrackedLocation(locationResult.getLastLocation(), false, userEmail);

                currentSession.updateSession(CurrentTrackingSession.TIME_INDICATION);
                if (service.isConnectedToNetwork()) {
                    Log.i("MyLog", "LocationServicePresenter - in onLocationResult() TIME - is connected to internet");
                    firestoreClient.sendLocationToServer(location);
                    location.setSent(true);
                    dbWorker.insertLocation(location);
                }
                else {
                    Log.i("MyLog", "LocationServicePresenter - in onLocationResult() TIME - internet is not working");
                    location.setSent(false);
                    dbWorker.insertLocation(location);
                    service.sendIntentToReceiver();
                }
            }
        };

        distanceLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(final LocationResult locationResult) {
                Log.i("MyLog", "LocationServicePresenter - in onLocationResult() DISTANCE");
                TrackedLocation location = new TrackedLocation(locationResult.getLastLocation(), false, userEmail);
                currentSession.updateSession(CurrentTrackingSession.DISTANCE_INDICATION);
                if (service.isConnectedToNetwork()) {
                    Log.i("MyLog", "LocationServicePresenter - in onLocationResult() DISTANCE - is connected to internet");
                    firestoreClient.sendLocationToServer(location);
                    location.setSent(true);
                    dbWorker.insertLocation(location);
                }
                else {
                    Log.i("MyLog", "LocationServicePresenter - in onLocationResult() DISTANCE - internet is not working");
                    location.setSent(false);
                    dbWorker.insertLocation(location);
                    service.sendIntentToReceiver();
                }
            }
        };
    }

    @Override
    public void startTracking() {
        Log.i("MyLog", "LocationServicePresenter - in startTracking()");
        locationClient.startLocationUpdate(timeLocationCallback, distanceLocationCallback);
    }

    @Override
    public void stopTracking() {
        Log.i("MyLog", "LocationServicePresenter - in stopTracking()");
        locationClient.stopLocationUpdate(timeLocationCallback, distanceLocationCallback);
    }

    @Override
    public void setUserEmail(String userEmail){
        Log.i("MyLog", "LocationServicePresenter - in setUserEmail()");
        this.userEmail = userEmail;
    }

    public void startForegroundService(){
        Log.i("MyLog", "LocationServicePresenter - in startForegroundService()");
        service.createNotificationChannel(CHANNEL_ID);
        service.startForegroundWithNotification(CHANNEL_ID);
    }
}
