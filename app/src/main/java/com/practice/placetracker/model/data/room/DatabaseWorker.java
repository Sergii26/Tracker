package com.practice.placetracker.model.data.room;

import android.content.Context;
import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DatabaseWorker {

    private LocationDatabase database;

    public DatabaseWorker(Context context) {
        database = LocationDatabase.getInstance(context);
    }

    public void showSizeInLog(){
        final Disposable disposable = Observable.just(new String(""))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i("MyLog", "DatabaseWorker - in showSizeInLog() accept, DB size = " + database.locationDao().getAllLocations().size());
                    }
                });
    }

    public void deleteAllLocations(){
        Disposable disposable = database.locationDao().deleteAllLocations()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i("MyLog", "DatabaseWorker - in deleteAllLocations() accept");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("MyLog", "DatabaseWorker - in deleteAllLocations() error = " + throwable.getMessage());
                    }
                });
    }

    public void insertLocation(TrackedLocation location) {
        Log.i("MyLog", "DatabaseWorker - in insertLocation() in start of method");
        Disposable disposable = database.locationDao().insertLocation(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i("MyLog", "DatabaseWorker - in insertLocation() on Success");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("MyLog", "DatabaseWorker - in insertLocation() on Error" + throwable.getMessage());
                    }
                });
    }

    public void deleteLocation(TrackedLocation location) {
        Disposable disposable = database.locationDao().deleteLocation(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i("MyLog", "DatabaseWorker - in deleteLocation() accept");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("MyLog", "DatabaseWorker - in deleteLocation() error = " + throwable.getMessage());
                    }
                });
    }

    public void updateLocation(boolean isSent, long uniqueId) {
        Disposable disposable = database.locationDao().update(isSent, uniqueId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i("MyLog", "DatabaseWorker - in updateLocation() accept");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("MyLog", "DatabaseWorker - in updateLocation() error = " + throwable.getMessage());
                    }
                });
    }

    public void deleteSentLocations(boolean isSent){
        Disposable disposable = database.locationDao().deleteSentLocations(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i("MyLog", "DatabaseWorker - in deleteSentLocations() accept");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("MyLog", "DatabaseWorker - in deleteSentLocations() error = " + throwable.getMessage());
                    }
                });
    }

    public void showDBinLog(){
        final Disposable disposable = Observable.just(new String(""))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                       List<TrackedLocation> locations = database.locationDao().getAllLocations();
                        Log.i("MyLog", "DatabaseWorker - in showDBinLog() accept SIZE = " + locations.size());
                        for(TrackedLocation location: locations) {
                            Log.i("MyLog", "DatabaseWorker - in showDBinLog() accept isSent = " + location.isSent() + ": uniqueId = " + location.getUniqueId());
                        }
                    }
                });
    }
}
