package com.practice.placetracker.model.data.room;

import com.practice.placetracker.App;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseWorkerModule {
    @Provides
    public DatabaseWorker provideDatabaseWorker(){
        return  new DatabaseWorker(App.getInstance().getAppContext());
    }
}
