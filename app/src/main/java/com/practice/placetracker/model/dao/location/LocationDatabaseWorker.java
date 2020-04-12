package com.practice.placetracker.model.dao.location;

import com.practice.placetracker.model.dao.TrackedLocationSchema;
import com.practice.placetracker.model.logger.ILog;

import java.util.List;

import io.reactivex.Completable;

public class LocationDatabaseWorker implements LocationDaoWorker {

    private final ILog logger;
    private final LocationDao database;

    public LocationDatabaseWorker(LocationDao database, ILog logger) {
        this.logger = logger;
        this.database = database;
    }

    public List<TrackedLocationSchema> getAllLocations() {
        logger.log("DatabaseWorker in getAllLocations()");
        return database.locationDao().getAllLocations();
    }

    public Completable insertLocation(TrackedLocationSchema location) {
        logger.log("DatabaseWorker in insertLocation()");
        return database.locationDao().insertLocation(location);
    }

    public Completable deleteLocation(TrackedLocationSchema location) {
        logger.log("DatabaseWorker in deleteLocation()");
        return database.locationDao().deleteLocation(location);
    }

    public List<TrackedLocationSchema> getLocationsBySent(boolean isSent) {
        logger.log("DatabaseWorker in getLocationsBySent()");
        return database.locationDao().getLocationsBySent(false);
    }

}

