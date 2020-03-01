package com.practice.placetracker.model.data.room;

import com.practice.placetracker.android_utils.ILog;
import com.practice.placetracker.android_utils.Logger;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DatabaseWorker {

    private final ILog logger = Logger.withTag("MyLog");

    private IDatabase database;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public DatabaseWorker(IDatabase database) {
        this.database = database;
        boolean isNull = false;
        if(database == null){
            isNull = true;
        }
    }

    public void showSizeInLog() {
        compositeDisposable.add(Observable.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        logger.log("DatabaseWorker in showSizeInLog() accept, DB size = " + database.locationDao().getAllLocations().size());
                    }
                }));
    }

    public void deleteAllLocations() {
        compositeDisposable.add(database.locationDao().deleteAllLocations()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        logger.log("DatabaseWorker in deleteAllLocations() accept");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        logger.log("DatabaseWorker in deleteAllLocations() error = " + throwable.getMessage());
                    }
                }));
    }

    public void insertLocation(TrackedLocation location) {
        logger.log("DatabaseWorker in insertLocation() in start of method");
        compositeDisposable.add(database.locationDao().insertLocation(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        logger.log("DatabaseWorker in insertLocation() on Success");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        logger.log("DatabaseWorker in insertLocation() on Error" + throwable.getMessage());
                    }
                }));
    }

    public void deleteLocation(TrackedLocation location) {
        compositeDisposable.add(database.locationDao().deleteLocation(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        logger.log("DatabaseWorker in deleteLocation() accept");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        logger.log("DatabaseWorker in deleteLocation() error = " + throwable.getMessage());
                    }
                }));
    }

    public void deleteLocationById(long uniqueId) {
        compositeDisposable.add(database.locationDao().deleteLocationById(uniqueId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        logger.log("DatabaseWorker in deleteLocationById() accept");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        logger.log("DatabaseWorker in deleteLocationById() error = " + throwable.getMessage());
                    }
                }));
    }

    public void updateLocation(boolean isSent, long uniqueId) {
        compositeDisposable.add(database.locationDao().update(isSent, uniqueId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        logger.log("DatabaseWorker in updateLocation() accept");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        logger.log("DatabaseWorker in in updateLocation() error = " + throwable.getMessage());
                    }
                }));
    }

    public void deleteSentLocations() {
        compositeDisposable.add(database.locationDao().deleteSentLocations(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        logger.log("DatabaseWorker in deleteSentLocations() accept");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        logger.log("DatabaseWorker in deleteSentLocations() error = " + throwable.getMessage());
                    }
                }));
    }

    public void showDBinLog() {
        compositeDisposable.add(Observable.just(new String(""))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        List<TrackedLocation> locations = database.locationDao().getAllLocations();
                        logger.log("DatabaseWorker in showDBinLog() accept SIZE = " + locations.size());
                        for (TrackedLocation location : locations) {
                            logger.log("DatabaseWorker in showDBinLog() accept isSent = " + location.isSent() + ": uniqueId = " + location.getUniqueId());
                        }
                    }
                }));
    }

    public List<TrackedLocation> getLocationsBySent(boolean isSent){
        return database.locationDao().getLocationsBySent(false);
    }


    public void disposeDisposable() {
        compositeDisposable.dispose();
    }
}
