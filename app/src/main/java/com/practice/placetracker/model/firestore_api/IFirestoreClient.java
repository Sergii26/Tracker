package com.practice.placetracker.model.firestore_api;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.practice.placetracker.model.data.room.TrackedLocation;

public interface IFirestoreClient {
    void sendLocationToServer(TrackedLocation location, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener);
}
