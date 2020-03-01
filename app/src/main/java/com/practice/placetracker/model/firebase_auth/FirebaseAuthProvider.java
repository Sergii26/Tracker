package com.practice.placetracker.model.firebase_auth;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthProvider implements IFirebaseAuthProvider {

    public FirebaseAuth getFirebaseAuthInstances(){
        return FirebaseAuth.getInstance();
    }
}
