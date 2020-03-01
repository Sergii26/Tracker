package com.practice.placetracker.model.firestore_api;

import dagger.Component;
import dagger.Module;

@Component(modules = {FirestoreClientModule.class})
public interface FirestoreClientComponent {
    IFirestoreClient provideFirestoreClient();
}
