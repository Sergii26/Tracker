package com.practice.placetracker.model.network.location;

import com.practice.placetracker.model.dao.TrackedLocationSchema;
import com.practice.placetracker.model.network.Result;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface LocationsNetwork {

    Single<Result<Boolean>> sendLocation(TrackedLocationSchema location);

    Single<Result<List<TrackedLocationSchema>>> getAllLocations(String collectionName);

    Observable<Integer> subscribeToFirestoreChanges(String collectionName);

    Single<Result<List<TrackedLocationSchema>>> getLocationsWhereTimeGreaterThen(String collectionName, long time);

    void unsubscribeFromFirestoreChanges();

}
