package com.practice.placetracker.model.dao.map;

import com.google.common.base.Optional;
import com.practice.placetracker.model.dao.TrackedLocationSchema;
import com.practice.placetracker.model.logger.ILog;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class MapDatabaseWorker implements MapDaoWorker {
    private final ILog logger;
    private final MapDao database;

    public MapDatabaseWorker(MapDao database, ILog logger) {
        this.logger = logger;
        this.database = database;
    }

    public Single<List<TrackedLocationSchema>> getAllUserLocations(String userEmail) {
        logger.log("MapDatabaseWorker getAllUserLocations()");
        return Single.fromCallable(() -> database.mapDao().getAllUserLocations(userEmail));
    }

    public Completable insertLocation(List<TrackedLocationSchema> locations) {
        logger.log("MapDatabaseWorker insertLocations()");
        return database.mapDao().insertLocation(locations);
    }

    public Single<Optional<TrackedLocationSchema>> getLastLocation(String userEmail) {
        logger.log("MapDatabaseWorker getLastLocation()");
        return Single.fromCallable(() -> database.mapDao().getLastLocation(userEmail))
                .map(trackedLocationSchemas -> {
                    if (trackedLocationSchemas.isEmpty()) {
                        return Optional.absent();
                    } else {
                        return Optional.of(trackedLocationSchemas.get(0));
                    }
                });
    }

}
