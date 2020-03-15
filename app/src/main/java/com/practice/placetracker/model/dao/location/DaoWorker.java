package com.practice.placetracker.model.dao.location;

import java.util.List;

import io.reactivex.Completable;

public interface DaoWorker {

    List<TrackedLocationSchema> getAllLocations();

    Completable insertLocation(TrackedLocationSchema location);

    Completable deleteLocation(TrackedLocationSchema location);

    Completable updateLocation(boolean isSent, long uniqueId);

    List<TrackedLocationSchema> getLocationsBySent(boolean isSent);

}
