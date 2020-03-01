package com.practice.placetracker.service;

import com.practice.placetracker.model.data.room.DatabaseWorkerModule;
import com.practice.placetracker.model.firestore_api.FirestoreClientModule;
import com.practice.placetracker.model.location_api.LocationClientModule;

import dagger.Component;

@Component(modules = {ServiceModule.class})
public interface ServiceComponent {
    void injectLocationService(LocationService service);
}
