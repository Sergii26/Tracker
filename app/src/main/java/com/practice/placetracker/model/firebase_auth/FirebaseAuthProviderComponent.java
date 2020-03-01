package com.practice.placetracker.model.firebase_auth;

import dagger.Component;

@Component(modules = {FirebaseAuthProviderModule.class})
public interface FirebaseAuthProviderComponent {
    FirebaseAuthProvider provideFirebaseAuthProvider();
}
