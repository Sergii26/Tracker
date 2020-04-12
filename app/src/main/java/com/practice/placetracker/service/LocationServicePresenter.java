package com.practice.placetracker.service;

import com.practice.placetracker.model.cache.SessionCache;
import com.practice.placetracker.model.dao.location.LocationDatabaseWorker;
import com.practice.placetracker.model.dao.TrackedLocationSchema;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.model.network.location.LocationsNetwork;
import com.practice.placetracker.model.prefs.Prefs;
import com.practice.placetracker.model.tracker.LocationsSupplier;
import com.practice.placetracker.model.tracker.TrackingResult;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class LocationServicePresenter implements LocationServiceContract.Presenter {

    private final ILog logger = Logger.withTag("MyLog");

    public static final long UPDATE_LOCATION_TIME = 1000 * 60 * 10;
    public static final float UPDATE_LOCATION_DISTANCE = 60;

    private final Prefs prefs;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final LocationServiceContract.Service service;
    private final LocationsSupplier locationsSupplier;
    private final SessionCache sessionCache;
    private final LocationsNetwork network;
    private final LocationDatabaseWorker dbWorker;

    public LocationServicePresenter(LocationServiceContract.Service service, LocationsSupplier locationsSupplier,
                                    LocationDatabaseWorker dbWorker, SessionCache sessionCache, Prefs prefs, LocationsNetwork network) {
        this.locationsSupplier = locationsSupplier;
        this.sessionCache = sessionCache;
        this.dbWorker = dbWorker;
        this.service = service;
        this.network = network;
        this.prefs = prefs;
        sessionCache.drop();
    }

    @Override
    public void startTracking() {
        logger.log("LocationServicePresenter in startTracking()");
        disposables.add(Observable.combineLatest(
                // reading from file
                Observable.just(prefs.getEmail())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()),

                locationsSupplier.getLocationsObservable(),
                new BiFunction<String, TrackingResult, TrackedLocationSchema>() {
                    @Override
                    public TrackedLocationSchema apply(String email, TrackingResult locationResult) throws Exception {
                        logger.log("LocationServicePresenter in onLocationResult() " + locationResult.getType());
                        sessionCache.updateSession(locationResult.getType());
                        return new TrackedLocationSchema(locationResult.getLocation(), false, email);
                    }
                })
                .subscribe(location -> {
                    if (service.isConnectedToNetwork()) {
                        logger.log("LocationServicePresenter in onLocationResult() - is connected to internet");
                        disposables.add(network.sendLocation(location)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(result -> {
                                    if (result.isFail()) {
                                        logger.log("LocationServicePresenter in onLocationResult() location sent - failure");
                                        disposables.add(insertToDatabase(location));
                                        service.scheduleJob();
                                    } else {
                                        logger.log("LocationServicePresenter in onLocationResult() location sent - success");
                                    }
                                }));
                    } else {
                        logger.log("LocationServicePresenter in onLocationResult() - internet is not working");
                        disposables.add(insertToDatabase(location));
                        service.scheduleJob();
                    }
                }));
        locationsSupplier.startLocationObserving();
    }

    @Override
    public void stopTracking() {
        logger.log("LocationServicePresenter in stopTracking()");
        locationsSupplier.stopLocationObserving();
        disposables.dispose();
    }

    public Disposable insertToDatabase(TrackedLocationSchema location){
        return dbWorker.insertLocation(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> logger.log("LocationServicePresenter insert location - success"), throwable -> logger.log("LocationServicePresenter insert location - failure"));
    }
}

