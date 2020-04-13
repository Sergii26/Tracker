package com.practice.placetracker.model.dao.map;

import com.google.common.base.Optional;
import com.practice.placetracker.model.dao.TrackedLocationSchema;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface MapDaoWorker {

    Single<List<TrackedLocationSchema>> getAllUserLocations(String userEmail);

    Completable insertLocation(List<TrackedLocationSchema> location);

    Single<Optional<TrackedLocationSchema>> getLastLocation(String userEmail);

}
