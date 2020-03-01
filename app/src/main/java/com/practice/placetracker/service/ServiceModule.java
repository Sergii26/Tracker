package com.practice.placetracker.service;

import com.practice.placetracker.App;
import com.practice.placetracker.AppComponent;
import com.practice.placetracker.model.data.room.DaggerDatabaseWorkerComponent;
import com.practice.placetracker.model.data.room.DatabaseWorkerComponent;
import com.practice.placetracker.model.firestore_api.DaggerFirestoreClientComponent;
import com.practice.placetracker.model.firestore_api.FirestoreClientComponent;
import com.practice.placetracker.model.location_api.DaggerLocationClientComponent;
import com.practice.placetracker.model.location_api.LocationClientComponent;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    private LocationServiceContract.Service service;

    public ServiceModule(LocationServiceContract.Service service) {
        this.service = service;
    }

    @Provides
    public LocationServiceContract.Presenter providePresenter(){
        final DatabaseWorkerComponent dbWorkerComponent = DaggerDatabaseWorkerComponent.create();
        final FirestoreClientComponent firestoreComponent = DaggerFirestoreClientComponent.create();
        final LocationClientComponent locationComponent = DaggerLocationClientComponent.create();
        return new LocationServicePresenter(service, locationComponent.provideLocationClient(), dbWorkerComponent.provideDatabaseWorker(), firestoreComponent.provideFirestoreClient());
    }
}
