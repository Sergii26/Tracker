package com.practice.placetracker.model.dao.map;

import com.google.common.base.Optional;
import com.practice.placetracker.model.dao.TrackedLocationSchema;
import com.practice.placetracker.model.logger.ILog;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class MapDatabaseWorker implements MapDaoWorker {
    private final ILog logger;
    private final MapRoomDao dao;

    public MapDatabaseWorker(MapRoomDao dao, ILog logger) {
        this.logger = logger;
        this.dao = dao;
    }

    public Single<List<TrackedLocationSchema>> getAllUserLocations(String userEmail) {
        logger.log("MapDatabaseWorker getAllUserLocations()");
        return Single.fromCallable(() -> dao.getAllUserLocations(userEmail));
    }

    public Completable insertLocation(List<TrackedLocationSchema> locations) {
        logger.log("MapDatabaseWorker insertLocations()");
        return dao.insertLocation(locations);
    }

    public Single<Optional<TrackedLocationSchema>> getLastLocation(String userEmail) {
        logger.log("MapDatabaseWorker getLastLocation()");
        return Single.fromCallable(() -> dao.getLastLocation(userEmail))
                .map(trackedLocationSchemas -> {
                    if (trackedLocationSchemas.isEmpty()) {
                        return Optional.absent();
                    } else {
                        return Optional.of(trackedLocationSchemas.get(0));
                    }
                });
    }

}
