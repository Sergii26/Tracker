package com.practice.placetracker.service;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.practice.placetracker.android_utils.ILog;
import com.practice.placetracker.android_utils.Logger;
import com.practice.placetracker.model.data.current_session.CurrentTrackingSession;
import com.practice.placetracker.model.data.room.DatabaseWorker;
import com.practice.placetracker.model.data.room.TrackedLocation;
import com.practice.placetracker.model.firestore_api.IFirestoreClient;
import com.practice.placetracker.model.location_api.ILocationClient;

public class LocationServicePresenter implements LocationServiceContract.Presenter {

    private final ILog logger = Logger.withTag("MyLog");

    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static final long UPDATE_LOCATION_TIME = 1000 * 60 * 10;
    public static final float UPDATE_LOCATION_DISTANCE = 60;

    private ILocationClient locationClient;
    private LocationCallback timeLocationCallback;
    private LocationCallback distanceLocationCallback;
    private IFirestoreClient firestoreClient;
    private DatabaseWorker dbWorker;
    private CurrentTrackingSession currentSession;
    private String userEmail;
    private LocationServiceContract.Service service;

    public LocationServicePresenter(LocationServiceContract.Service service, ILocationClient locationClient, DatabaseWorker dbWorker, IFirestoreClient firestoreClient) {
        this.service = service;
        this.locationClient = locationClient;
        this.firestoreClient = firestoreClient;
        this.dbWorker = dbWorker;
        this.currentSession = CurrentTrackingSession.getInstance();
        currentSession.clearCurrentSession();
        createLocationCallbacks();
    }

    private void createLocationCallbacks() {
        timeLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(final LocationResult locationResult) {
                logger.log("LocationServicePresenter in onLocationResult() TIME");
                dbWorker.showSizeInLog();
                TrackedLocation location = new TrackedLocation(locationResult.getLastLocation(), false, userEmail);
                currentSession.updateSession(CurrentTrackingSession.TIME_INDICATION);
                if (service.isConnectedToNetwork()) {
                    logger.log("LocationServicePresenter in onLocationResult() TIME - is connected to internet");
                    firestoreClient.sendLocationToServer(location, o -> {
                        logger.log("LocationServicePresenter in onLocationResult() TIME location sent - success");
                        location.setSent(true);
                        dbWorker.insertLocation(location);
                        dbWorker.deleteSentLocations();
                    }, e -> {
                        logger.log("LocationServicePresenter in onLocationResult() TIME location sent - failure");
                        dbWorker.insertLocation(location);
                    });
                } else {
                    logger.log("LocationServicePresenter in onLocationResult() TIME - internet is not working");
                    location.setSent(false);
                    dbWorker.insertLocation(location);
                    service.sendIntentToReceiver();
                }
            }
        };

        distanceLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(final LocationResult locationResult) {
                logger.log("LocationServicePresenter in onLocationResult() DISTANCE");
                TrackedLocation location = new TrackedLocation(locationResult.getLastLocation(), false, userEmail);
                currentSession.updateSession(CurrentTrackingSession.DISTANCE_INDICATION);
                if (service.isConnectedToNetwork()) {
                    logger.log("LocationServicePresenter in onLocationResult() DISTANCE - is connected to internet");
                    firestoreClient.sendLocationToServer(location, o -> {
                        logger.log("LocationServicePresenter in onLocationResult() DISTANCE location sent - success");
                        location.setSent(true);
                        dbWorker.insertLocation(location);
                        dbWorker.deleteSentLocations();
                    }, e -> {
                        logger.log("LocationServicePresenter in onLocationResult() DISTANCE location sent - failure");
                        dbWorker.insertLocation(location);
                    });
                } else {
                    logger.log("LocationServicePresenter in onLocationResult() DISTANCE - internet is not working");
                    location.setSent(false);
                    dbWorker.insertLocation(location);
                    service.sendIntentToReceiver();
                }
            }
        };
    }

    @Override
    public void startTracking() {
        logger.log("LocationServicePresenter in startTracking()");
        locationClient.startLocationUpdateByTime(timeLocationCallback);
        locationClient.startLocationUpdateByDistance(distanceLocationCallback);
    }

    @Override
    public void stopTracking() {
        logger.log("LocationServicePresenter in stopTracking()");
        locationClient.stopLocationUpdateByTime(timeLocationCallback);
        locationClient.stopLocationUpdateByDistance(distanceLocationCallback);
    }

    @Override
    public void setUserEmail(String userEmail) {
        logger.log("LocationServicePresenter in in setUserEmail()");
        this.userEmail = userEmail;
    }

    public void startForegroundService() {
        logger.log("LocationServicePresenter in in startForegroundService()");
        service.createNotificationChannel(CHANNEL_ID);
        service.startForegroundWithNotification(CHANNEL_ID);
    }

    public void disposeDisposable() {
        dbWorker.disposeDisposable();
    }
}
