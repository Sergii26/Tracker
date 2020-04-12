package com.practice.placetracker.model.network.location;

import com.google.firebase.firestore.FirebaseFirestore;
import com.practice.placetracker.model.dao.location.TrackedLocationSchema;
import com.practice.placetracker.model.network.Result;

import io.reactivex.Single;

public class FirebaseLocationsNetwork implements LocationsNetwork {

    private final FirebaseFirestore firebase;

    public FirebaseLocationsNetwork() {
        firebase = FirebaseFirestore.getInstance();
    }

    @Override
    public Single<Result<Boolean>> sendLocation(TrackedLocationSchema location) {

        return Single.just(firebase.collection(location.getUserEmail())
                .add(location))
                .map(task -> {
                    if (task.isSuccessful()) {
                        return new Result<>(true);
                    } else {
                        return new Result<>(task.getException());
                    }
                });
    }
}

