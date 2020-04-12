package com.practice.placetracker.model.dao.map;

import com.practice.placetracker.model.dao.TrackedLocationSchema;
import com.practice.placetracker.model.logger.ILog;

import java.util.List;

import io.reactivex.Completable;

public class MapDatabaseWorker implements MapDaoWorker {
    private final ILog logger;
    private final MapDao database;

    public MapDatabaseWorker(MapDao database, ILog logger) {
        this.logger = logger;
        this.database = database;
    }

    public List<TrackedLocationSchema> getAllUserLocations(String userEmail) {
        logger.log("MapDatabaseWorker getAllUserLocations()");
        return database.mapDao().getAllUserLocations(userEmail);
    }

    public Completable insertLocation(List<TrackedLocationSchema> locations) {
        logger.log("MapDatabaseWorker insertLocations()");
        return database.mapDao().insertLocation(locations);
    }

    public TrackedLocationSchema getLastLocation(String userEmail){
        logger.log("MapDatabaseWorker getLastLocation()");
        final List<TrackedLocationSchema> locations = database.mapDao().getLastLocation(userEmail);
        if(locations.isEmpty()){
            return null;
        } else {
            return locations.get(0);
        }
    }

}
