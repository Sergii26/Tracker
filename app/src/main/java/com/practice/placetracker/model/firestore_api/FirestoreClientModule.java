package com.practice.placetracker.model.firestore_api;

import dagger.Module;
import dagger.Provides;

@Module
public class FirestoreClientModule {

    @Provides
    IFirestoreClient provideFirestoreClient(){
        return new FirestoreClient();
    }

}
