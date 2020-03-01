package com.practice.placetracker;

import com.practice.placetracker.model.data.room.IDatabase;
import com.practice.placetracker.model.data.room.LocationDatabase;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final LocationDatabase database;

    public AppModule() {
        database = LocationDatabase.getInstance(App.getInstance().getAppContext());
    }

    @Provides
    @Singleton
    public IDatabase provideLocationDatabase(){
        return database;
    }
}

