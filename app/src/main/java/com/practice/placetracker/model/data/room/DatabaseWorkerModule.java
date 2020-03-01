package com.practice.placetracker.model.data.room;

import com.practice.placetracker.App;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseWorkerModule {
    @Inject
    IDatabase database;

    public DatabaseWorkerModule(){
        App.getInstance().getAppComponent().injectLocationDatabase(this);
    }

    @Provides
    public DatabaseWorker provideDatabaseWorker(){
        return  new DatabaseWorker(database);
    }
}
