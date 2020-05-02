package com.practice.placetracker;

import com.practice.placetracker.model.cache.SessionCache;
import com.practice.placetracker.model.cache.SessionCacheImpl;
import com.practice.placetracker.model.dao.location.LocationDao;
import com.practice.placetracker.model.dao.location.LocationDaoWorker;
import com.practice.placetracker.model.dao.location.LocationDatabase;
import com.practice.placetracker.model.dao.location.LocationDatabaseWorker;
import com.practice.placetracker.model.dao.map.MapDao;
import com.practice.placetracker.model.dao.map.MapDaoWorker;
import com.practice.placetracker.model.dao.map.MapDatabase;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.model.network.Result;
import com.practice.placetracker.model.network.auth.AuthNetwork;
import com.practice.placetracker.model.network.auth.FirebaseAuthNetwork;
import com.practice.placetracker.model.dao.MapDatabaseWorkerTest;
import com.practice.placetracker.model.network.location.FirebaseLocationNetworkTest;
import com.practice.placetracker.model.network.location.LocationsNetwork;
import com.practice.placetracker.model.prefs.Prefs;
import com.practice.placetracker.model.prefs.PrefsImpl;
import com.practice.placetracker.model.tracker.LocationClient;
import com.practice.placetracker.model.tracker.LocationsSupplier;
import com.practice.placetracker.model.use_case.sender.SendSavedLocationsUseCase;
import com.practice.placetracker.model.use_case.UseCase;
import com.practice.placetracker.service.LocationServicePresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;

@Module
public class AppTestModule {

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
        return new FirebaseLocationNetworkTest();
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
    @Singleton
    public MapDao provideMapDao() {
        return MapDatabase.newInstance(App.getInstance());
    }

    @Provides
    public LocationDaoWorker provideLocationDatabaseWorker(){
        return new LocationDatabaseWorker(App.getInstance().getAppComponent().getLocationDao().locationDao(), Logger.withTag("MyLog"));
    }

    @Provides
    public MapDaoWorker provideMapDatabaseWorker(){
        return new MapDatabaseWorkerTest();
    }

    @Provides
    public UseCase<Void, Observable<Result<Boolean>>> provideSendSaveLocationsUseCase(){
        return new SendSavedLocationsUseCase(App.getInstance().getAppComponent().getLocationsNetwork(),
                App.getInstance().getAppComponent().provideLocationDatabaseWorker(), Logger.withTag("MyLog"));
    }
}
