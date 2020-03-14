package com.practice.placetracker.model.dao.location;

import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DatabaseWorker {

    private final ILog logger = Logger.withTag("MyLog");
    private final LocationDao database;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public DatabaseWorker(LocationDao database) {
        this.database = database;
    }

    public void showSizeInLog() {
        compositeDisposable.add(Observable.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(s -> logger.log("DatabaseWorker in showSizeInLog() accept, DB size = " + database.locationDao().getAllLocations().size())));
    }

    public void deleteAllLocations() {
        compositeDisposable.add(database.locationDao().deleteAllLocations()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> logger.log("DatabaseWorker in deleteAllLocations() accept"), throwable -> logger.log("DatabaseWorker in deleteAllLocations() error = " + throwable.getMessage())));
    }

    public void insertLocation(TrackedLocationSchema location) {
        logger.log("DatabaseWorker in insertLocation() in start of method");
        compositeDisposable.add(database.locationDao().insertLocation(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> logger.log("DatabaseWorker in insertLocation() on Success"), throwable -> logger.log("DatabaseWorker in insertLocation() on Error" + throwable.getMessage())));
    }

    public void deleteLocation(TrackedLocationSchema location) {
        compositeDisposable.add(database.locationDao().deleteLocation(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> logger.log("DatabaseWorker in deleteLocation() accept"), throwable -> logger.log("DatabaseWorker in deleteLocation() error = " + throwable.getMessage())));
    }

    public void deleteLocationById(long uniqueId) {
        compositeDisposable.add(database.locationDao().deleteLocationById(uniqueId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> logger.log("DatabaseWorker in deleteLocationById() accept"), throwable -> logger.log("DatabaseWorker in deleteLocationById() error = " + throwable.getMessage())));
    }

    public void updateLocation(boolean isSent, long uniqueId) {
        compositeDisposable.add(database.locationDao().update(isSent, uniqueId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> logger.log("DatabaseWorker in updateLocation() accept"), throwable -> logger.log("DatabaseWorker in in updateLocation() error = " + throwable.getMessage())));
    }

    public void deleteSentLocations() {
        compositeDisposable.add(database.locationDao().deleteSentLocations(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> logger.log("DatabaseWorker in deleteSentLocations() accept"), throwable -> logger.log("DatabaseWorker in deleteSentLocations() error = " + throwable.getMessage())));
    }

    public void showDBinLog() {
        compositeDisposable.add(Observable.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(s -> {
                    List<TrackedLocationSchema> locations = database.locationDao().getAllLocations();
                    logger.log("DatabaseWorker in showDBinLog() accept SIZE = " + locations.size());
                    for (TrackedLocationSchema location : locations) {
                        logger.log("DatabaseWorker in showDBinLog() accept isSent = " + location.isSent() + ": uniqueId = " + location.getUniqueId());
                    }
                }));
    }

    public List<TrackedLocationSchema> getLocationsBySent(boolean isSent) {
        return database.locationDao().getLocationsBySent(false);
    }

    public void disposeDisposable() {
        compositeDisposable.dispose();
    }
}

