package com.practice.placetracker.model.dao.location;

import com.practice.placetracker.model.dao.TrackedLocationSchema;

import java.util.List;

import io.reactivex.Completable;

public interface LocationDaoWorker {

    List<TrackedLocationSchema> getAllLocations();

    Completable insertLocation(TrackedLocationSchema location);

    Completable deleteLocation(TrackedLocationSchema location);

    List<TrackedLocationSchema> getLocationsBySent(boolean isSent);

}
