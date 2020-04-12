package com.practice.placetracker.model.dao.map;

import com.practice.placetracker.model.dao.TrackedLocationSchema;

import java.util.List;

import io.reactivex.Completable;

public interface MapDaoWorker {

    List<TrackedLocationSchema> getAllUserLocations(String userEmail);

    Completable insertLocation(List<TrackedLocationSchema> location);

    TrackedLocationSchema getLastLocation(String userEmail);

}
