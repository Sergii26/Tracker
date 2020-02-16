package com.practice.placetracker;

import com.practice.placetracker.model.data.room.DatabaseWorker;
import com.practice.placetracker.model.data.room.DatabaseWorkerModule;
import com.practice.placetracker.model.location_api.LocationClient;
import com.practice.placetracker.model.location_api.LocationClientModule;

import dagger.Component;

@Component(modules = {DatabaseWorkerModule.class, LocationClientModule.class})
public interface AppComponent {
    public DatabaseWorker provideDatabaseWorker();
    public LocationClient provideLocationClient();
}
