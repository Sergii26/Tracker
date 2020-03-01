package com.practice.placetracker;

import com.practice.placetracker.model.data.room.DatabaseWorkerModule;
import com.practice.placetracker.ui.location_fragment.LocationFragmentModule;

import javax.inject.Singleton;

import dagger.Component;


//@Component(modules = {DatabaseWorkerModule.class, LocationClientModule.class, FirestoreClientModule.class})
//public interface AppComponent {
//    public DatabaseWorker provideDatabaseWorker();
//    public ILocationClient provideLocationClient();
//    public IFirestoreClient provideFirestoreClient();


@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    public void injectLocationDatabase(DatabaseWorkerModule module);
    public void injectLocationFragment(LocationFragmentModule module);
}


