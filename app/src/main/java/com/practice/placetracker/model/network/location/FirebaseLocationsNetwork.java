package com.practice.placetracker.model.network.location;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.practice.placetracker.model.dao.TrackedLocationSchema;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.model.network.Result;

import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;

public class FirebaseLocationsNetwork implements LocationsNetwork {

    private final ILog logger = Logger.withTag("MyLog");

    private final FirebaseFirestore firebase;

    private final BehaviorSubject<Integer> isChanged = BehaviorSubject.createDefault(0);

    private ListenerRegistration registration;

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

    public Single<Result<List<TrackedLocationSchema>>> getAllLocations(String collectionName) {
        return Single.fromCallable(() -> {
            try {
                final QuerySnapshot query = Tasks.await(firebase.collection(collectionName).orderBy("time").get());
                logger.log("FirebaseLocationsNetwork in getAllLocations() success");
                return new Result<>(query.toObjects(TrackedLocationSchema.class));
            } catch (Throwable e) {
                logger.log("FirebaseLocationsNetwork in getAllLocations() error: " + e.getMessage());
                return new Result<>(e);
            }
        });
    }

    public Single<Result<List<TrackedLocationSchema>>> getLocationsWhereTimeGreaterThen(String collectionName, long time) {
        return Single.fromCallable(() -> {
            try {
                final QuerySnapshot query = Tasks.await(firebase.collection(collectionName).whereGreaterThan("time", time).get());
                logger.log("FirebaseLocationsNetwork in getLocationsWhereTimeGreaterThen() success ");
                return new Result<>(query.toObjects(TrackedLocationSchema.class));
            } catch (Throwable e) {
                logger.log("FirebaseLocationsNetwork in getLocationsWhereTimeGreaterThen() error: " + e.getMessage());
                return new Result<>(e);
            }
        });
    }

    private void startFirestoreObserving(String collectionName){
        logger.log("FirebaseLocationsNetwork startFirestoreObserving()");
        registration = firebase.collection(collectionName).addSnapshotListener((queryDocumentSnapshots, e) -> {
            logger.log("FirebaseLocationsNetwork startFirestoreObserving() new Locations trigger");
                    Objects.requireNonNull(queryDocumentSnapshots).getDocumentChanges();
                    isChanged.onNext(isChanged.getValue() + 1);
        });
    }

    public void unsubscribeFromFirestoreChanges(){
        logger.log("FirebaseLocationsNetwork unsubscribeToFirestoreChanges()");
        if(registration != null){
            registration.remove();
        }
    }

    public Observable<Integer> subscribeToFirestoreChanges(String collectionName) {
        logger.log("FirebaseLocationsNetwork subscribeToFirestoreChanges()");
        startFirestoreObserving(collectionName);
        return isChanged;
    }

}

