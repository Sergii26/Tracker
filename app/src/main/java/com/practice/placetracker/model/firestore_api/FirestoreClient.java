package com.practice.placetracker.model.firestore_api;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.practice.placetracker.android_utils.ILog;
import com.practice.placetracker.android_utils.Logger;
import com.practice.placetracker.model.data.room.TrackedLocation;

public class FirestoreClient implements IFirestoreClient {

    private final ILog logger = Logger.withTag("MyLog");

    private FirebaseFirestore firebase;

    public FirestoreClient() {
        firebase = FirebaseFirestore.getInstance();
    }

    public void sendLocationToServer(TrackedLocation location, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener) {
        firebase.collection(location.getUserEmail())
                .add(location)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

}
