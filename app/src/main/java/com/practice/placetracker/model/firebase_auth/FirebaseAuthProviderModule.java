package com.practice.placetracker.model.firebase_auth;

import com.google.firebase.auth.FirebaseAuth;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseAuthProviderModule {
    @Provides
    FirebaseAuthProvider provideFirebaseAuthProvider(){
        return new FirebaseAuthProvider();
    }
}
