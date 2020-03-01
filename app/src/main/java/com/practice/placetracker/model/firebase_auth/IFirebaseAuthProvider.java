package com.practice.placetracker.model.firebase_auth;

import com.google.firebase.auth.FirebaseAuth;

public interface IFirebaseAuthProvider {
    FirebaseAuth getFirebaseAuthInstances();
}
