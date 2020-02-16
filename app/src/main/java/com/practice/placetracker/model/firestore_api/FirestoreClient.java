package com.practice.placetracker.model.firestore_api;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.practice.placetracker.model.data.room.TrackedLocation;

import androidx.annotation.NonNull;

public class FirestoreClient {

    private FirebaseFirestore firebase;
    private String collectionName;

    public FirestoreClient() {
        firebase = FirebaseFirestore.getInstance();
    }

    public void sendLocationToServer(TrackedLocation location) {
        firebase.collection(location.getUserEmail())
                .add(location)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("MyLog", "CFS - onSuccess");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("MyLog", "CFS - onFailure");
                    }
                });
    }

}
