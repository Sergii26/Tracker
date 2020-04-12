package com.practice.placetracker.ui.map.map;

import com.practice.placetracker.R;
import com.practice.placetracker.model.dao.TrackedLocationSchema;
import com.practice.placetracker.model.dao.map.MapDaoWorker;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.network.Result;
import com.practice.placetracker.model.network.auth.AuthNetwork;
import com.practice.placetracker.model.network.location.LocationsNetwork;
import com.practice.placetracker.model.prefs.Prefs;
import com.practice.placetracker.ui.arch.MvpPresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MapPresenter extends MvpPresenter<MapContract.View> implements MapContract.Presenter {
    private final ILog logger;
    private final AuthNetwork authNetwork;
    private final LocationsNetwork locationNetwork;
    private final Prefs prefs;
    private final MapDaoWorker dbWorker;
    private TrackedLocationSchema lastLocation;
    private boolean isSubscribed = false;

    public MapPresenter(AuthNetwork authNetwork, LocationsNetwork locationNetwork, Prefs prefs, MapDaoWorker dbWorker, ILog logger) {
        this.authNetwork = authNetwork;
        this.locationNetwork = locationNetwork;
        this.prefs = prefs;
        this.dbWorker = dbWorker;
        this.logger = logger;
    }

    @Override
    public void showMap() {
        view.initMap();
    }

    @Override
    public void onNetworkConnectionChange() {
        logger.log("MapPresenter onNetworkConnectionChange()");
        if (view.isConnectedToNetwork()) {
            subscribeToNewLocations();
        } else {
            unsubscribeFromFirestore();
        }
    }

    private void unsubscribeFromFirestore() {
        logger.log("MapPresenter unsubscribeFromFirestore()");
        if (isSubscribed) {
            isSubscribed = false;
            locationNetwork.unsubscribeFromFirestoreChanges();
        }
    }

    @Override
    public void showLocations() {
        logger.log("MapPresenter showLocations()");
        showLocationsFromDatabase();
        if (view.isConnectedToNetwork()) {
            subscribeToNewLocations();
        }
    }

    private void subscribeToNewLocations() {
        logger.log("MapPresenter subscribeToNewLocations()");
        if (isSubscribed) {
            return;
        }
        isSubscribed = true;
        onStopDisposable.add(locationNetwork.subscribeToFirestoreChanges(prefs.getEmail())
                .throttleLast(1000, TimeUnit.MILLISECONDS)
                .subscribe(integer -> {
                    showLocationsAndAddToDatabase();
                }));
    }

    private void showLocationsAndAddToDatabase() {
        onStopDisposable.add(Single.fromCallable(() -> {
            TrackedLocationSchema location = dbWorker.getLastLocation(prefs.getEmail());
            if (location == null) {
                // if location == null => database is empty so download all locations (locations with field "time" greater than 0)
                location = new TrackedLocationSchema(0, 0, 0, 0, 0, false, prefs.getEmail());
            }
            return location;
        })
                .flatMap((Function<TrackedLocationSchema, Single<Result<List<TrackedLocationSchema>>>>) trackedLocationSchema -> locationNetwork
                        .getLocationsWhereTimeGreaterThen(prefs.getEmail(), trackedLocationSchema.getTime()))
                .map(listResult -> {
                    if (listResult.isFail()) {
                        logger.log("MapPresenter showLocationsAndInsertToDatabase() in listResult error: " + listResult.getError().getMessage());
                    } else if (!listResult.getData().isEmpty()) {
                        //insert new locations
                        dbWorker.insertLocation(listResult.getData()).doOnComplete(() -> logger.log("MapPresenter showLocationsAndInsertToDatabase() insert to db complete"))
                                .doOnError(throwable -> logger.log("MapPresenter showLocationsAndInsertToDatabase() insert to db error: " + throwable.getMessage()))
                                .subscribe();
                    }
                    return listResult;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listResult -> {
                    if (listResult.isFail()) {
                        logger.log("MapPresenter showLocationsAndInsertToDatabase() in listResult error: " + listResult.getError().getMessage());
                    } else if (hasView() && !listResult.getData().isEmpty()) {
                        view.addPolylines(listResult.getData());
                        view.setCameraPosition(listResult.getData().get(listResult.getData().size() - 1), 16);
                    }
                }, throwable -> logger.log("MapPresenter showLocationsAndInsertToDatabase() error: " + throwable.getMessage())));
    }

    private void showLocationsFromDatabase() {
        logger.log("MapPresenter showLocationsFromDatabase()");
        onStopDisposable.add(Single.fromCallable(() -> dbWorker.getAllUserLocations(prefs.getEmail()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trackedLocationSchemas -> {
                    if (trackedLocationSchemas.isEmpty()) {
                        view.showToast(R.string.no_locations_in_database);
                    } else {
                        view.addPolylines(trackedLocationSchemas);
                        view.setCameraPosition(trackedLocationSchemas.get(trackedLocationSchemas.size() - 1), 16);
                    }
                }, throwable -> logger.log("MapPresenter showLocationsFromDatabase() error: " + throwable.getMessage())));
    }

    public void setLastLocation(TrackedLocationSchema location) {
        this.lastLocation = location;
    }

    public TrackedLocationSchema getLastLocation() {
        return lastLocation;
    }

    @Override
    public void logOut() {
        logger.log("MapPresenter in logOut()");
        prefs.putEmail("");
        authNetwork.logOut();
    }

    @Override
    public void unsubscribe() {
        logger.log("MapPresenter in unsubscribe()");
        unsubscribeFromFirestore();
        super.unsubscribe();
    }

}
