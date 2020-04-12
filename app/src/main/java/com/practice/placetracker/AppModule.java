package com.practice.placetracker;

import com.practice.placetracker.model.cache.SessionCache;
import com.practice.placetracker.model.cache.SessionCacheImpl;
import com.practice.placetracker.model.dao.location.DaoWorker;
import com.practice.placetracker.model.dao.location.DatabaseWorker;
import com.practice.placetracker.model.dao.location.LocationDao;
import com.practice.placetracker.model.dao.location.LocationDatabase;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.model.network.auth.AuthNetwork;
import com.practice.placetracker.model.network.auth.FirebaseAuthNetwork;
import com.practice.placetracker.model.network.location.FirebaseLocationsNetwork;
import com.practice.placetracker.model.network.location.LocationsNetwork;
import com.practice.placetracker.model.prefs.Prefs;
import com.practice.placetracker.model.prefs.PrefsImpl;
import com.practice.placetracker.model.tracker.LocationClient;
import com.practice.placetracker.model.tracker.LocationsSupplier;
import com.practice.placetracker.service.LocationServicePresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    @Singleton
    public AuthNetwork provideAuthNetwork() {
        return new FirebaseAuthNetwork();
    }

    @Provides
    @Singleton
    public Prefs providePreferences() {
        return new PrefsImpl(App.getInstance());
    }

    @Provides
    @Singleton
    public LocationsNetwork provideLocationsNetwork() {
        return new FirebaseLocationsNetwork();
    }

    @Provides
    @Singleton
    public SessionCache provideSessionCache() {
        return new SessionCacheImpl();
    }

    @Provides
    public LocationsSupplier provideLocationsSupplier() {
        return new LocationClient(App.getInstance(), LocationServicePresenter.UPDATE_LOCATION_TIME,
                LocationServicePresenter.UPDATE_LOCATION_DISTANCE, Logger.withTag("MyLog"));
    }

    @Provides
    @Singleton
    public LocationDao provideLocationDao() {
        return LocationDatabase.newInstance(App.getInstance());
    }

    @Provides
    public DaoWorker provideDatabaseWorker(){
        return new DatabaseWorker(App.getInstance().getAppComponent().getLocationDao(), Logger.withTag("MyLog"));
    }
}


