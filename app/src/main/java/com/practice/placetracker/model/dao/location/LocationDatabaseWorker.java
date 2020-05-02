package com.practice.placetracker.model.dao.location;

import com.practice.placetracker.model.dao.TrackedLocationSchema;
import com.practice.placetracker.model.logger.ILog;

import java.util.List;

import io.reactivex.Completable;

public class LocationDatabaseWorker implements LocationDaoWorker {

    private final ILog logger;
    private final LocationRoomDao dao;

    public LocationDatabaseWorker(LocationRoomDao dao, ILog logger) {
        this.logger = logger;
        this.dao = dao;
    }

    public List<TrackedLocationSchema> getAllLocations() {
        logger.log("DatabaseWorker in getAllLocations()");
        return dao.getAllLocations();
    }

    public Completable insertLocation(TrackedLocationSchema location) {
        logger.log("DatabaseWorker in insertLocation()");
        return dao.insertLocation(location);
    }

    public Completable deleteLocation(TrackedLocationSchema location) {
        logger.log("DatabaseWorker in deleteLocation()");
        return dao.deleteLocation(location);
    }

    public List<TrackedLocationSchema> getLocationsBySent(boolean isSent) {
        logger.log("DatabaseWorker in getLocationsBySent()");
        return dao.getLocationsBySent(false);
    }

}

