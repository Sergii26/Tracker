package com.practice.placetracker.service;

import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.model.cache.SessionCache;
import com.practice.placetracker.model.dao.location.DatabaseWorker;
import com.practice.placetracker.model.dao.location.TrackedLocationSchema;
import com.practice.placetracker.model.network.Result;
import com.practice.placetracker.model.network.location.LocationsNetwork;
import com.practice.placetracker.model.prefs.Prefs;
import com.practice.placetracker.model.tracker.LocationsSupplier;
import com.practice.placetracker.model.tracker.TrackingResult;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LocationServicePresenter implements LocationServiceContract.Presenter {

    private final ILog logger = Logger.withTag("MyLog");

    public static final long UPDATE_LOCATION_TIME = 1000 * 30*1;
    public static final float UPDATE_LOCATION_DISTANCE = 60;

    private final Prefs prefs;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final LocationServiceContract.Service service;
    private final LocationsSupplier locationsSupplier;
    private final SessionCache sessionCache;
    private final LocationsNetwork network;
    private final DatabaseWorker dbWorker;

    public LocationServicePresenter(LocationServiceContract.Service service, LocationsSupplier locationsSupplier,
                                    DatabaseWorker dbWorker, SessionCache sessionCache, Prefs prefs, LocationsNetwork network) {
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
                .subscribe(new Consumer<TrackedLocationSchema>() {
                    @Override
                    public void accept(TrackedLocationSchema location) throws Exception {
                        dbWorker.showSizeInLog();
                        if (service.isConnectedToNetwork()) {
                            logger.log("LocationServicePresenter in onLocationResult() - is connected to internet");
                            disposables.add(network.sendLocation(location)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Result<Boolean>>() {
                                        @Override
                                        public void accept(Result<Boolean> result) throws Exception {
                                            if (result.isFail()) {
                                                logger.log("LocationServicePresenter in onLocationResult() location sent - failure");
                                                service.scheduleJob();
                                            } else {
                                                logger.log("LocationServicePresenter in onLocationResult() location sent - success");
//                                                // todo sync this actions: they are async!
//                                                // this code was written only for inform LocationFragmentPresenter about new location via DB
//                                                // now presenter has information from sessionCache
                                            }
                                        }
                                    }));
                        } else {
                            logger.log("LocationServicePresenter in onLocationResult() - internet is not working");
                            dbWorker.insertLocation(location);
                            service.scheduleJob();
                        }
                    }
                }));
        locationsSupplier.startLocationObserving();
    }

    @Override
    public void stopTracking() {
        logger.log("LocationServicePresenter in stopTracking()");
        locationsSupplier.stopLocationObserving();
        dbWorker.disposeDisposable();
    }
}

