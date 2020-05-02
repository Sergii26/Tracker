package com.practice.placetracker.model.network.location;

import com.practice.placetracker.model.dao.TrackedLocationSchema;
import com.practice.placetracker.model.network.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class FirebaseLocationNetworkTest implements LocationsNetwork {

    private final BehaviorSubject<Integer> isChanged = BehaviorSubject.createDefault(0);
    private List<TrackedLocationSchema> locations;

    public FirebaseLocationNetworkTest() {
        initLocations();
    }

    private void initLocations() {
        locations = new ArrayList<>();
        locations.add(new TrackedLocationSchema(0, 50.4545077, 30.3963479,
                1587448628611L, 1, false, "test@test.ua"));
        locations.add(new TrackedLocationSchema(0, 50.4550534, 30.396309,
                1587448659146L, 1, false, "test@test.ua"));
        locations.add(new TrackedLocationSchema(0, 50.4556213, 30.3962601,
                1587448698086L, 1, false, "test@test.ua"));
    }

    @Override
    public Single<Result<Boolean>> sendLocation(TrackedLocationSchema location) {
        return null;
    }

    @Override
    public Single<Result<List<TrackedLocationSchema>>> getAllLocations(String collectionName) {
        return Single.just(new Result<>(locations));
    }

    @Override
    public Observable<Integer> subscribeToFirestoreChanges(String collectionName) {
        return isChanged;
    }

    @Override
    public Single<Result<List<TrackedLocationSchema>>> getLocationsWhereTimeGreaterThen(String collectionName, long time) {
        if (time == 0) {
            return Single.just(new Result<>(locations));
        } else {
            return Single.just(new Result<>(new ArrayList<>()));
        }
    }


    @Override
    public void unsubscribeFromFirestoreChanges() {

    }

}
