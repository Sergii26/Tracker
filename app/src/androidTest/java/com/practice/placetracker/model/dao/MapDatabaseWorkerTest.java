package com.practice.placetracker.model.dao;

import com.google.common.base.Optional;
import com.practice.placetracker.model.dao.map.MapDaoWorker;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class MapDatabaseWorkerTest implements MapDaoWorker {

    private List<TrackedLocationSchema> locations;

    public MapDatabaseWorkerTest(){
        locations = new ArrayList<>();
    }

    @Override
    public Single<List<TrackedLocationSchema>> getAllUserLocations(String userEmail) {
            return Single.fromCallable(() -> locations);
    }

    @Override
    public Completable insertLocation(List<TrackedLocationSchema> location) {
        locations.addAll(location);
        return Completable.complete();
    }

    @Override
    public Single<Optional<TrackedLocationSchema>> getLastLocation(String userEmail) {
        if(locations.isEmpty()) {
            return Single.just(Optional.absent());
        } else {
            return Single.just(Optional.of(locations.get(locations.size() - 1)));
        }
    }
}
