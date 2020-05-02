package com.practice.placetracker;

import com.practice.placetracker.model.cache.SessionCache;
import com.practice.placetracker.model.dao.location.LocationDaoWorker;
import com.practice.placetracker.model.dao.location.LocationDao;
import com.practice.placetracker.model.dao.map.MapDao;
import com.practice.placetracker.model.dao.map.MapDaoWorker;
import com.practice.placetracker.model.network.auth.AuthNetwork;
import com.practice.placetracker.model.network.location.LocationsNetwork;
import com.practice.placetracker.model.prefs.Prefs;
import com.practice.placetracker.model.tracker.LocationsSupplier;
import com.practice.placetracker.model.use_case.SavedLocationsSender;
import com.practice.placetracker.ui.map.map.MapFragmentModule;
import com.practice.placetracker.ui.tracker.location.LocationFragmentModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void injectLocationFragment(LocationFragmentModule module);

    void injectMapFragment(MapFragmentModule module);

    AuthNetwork getAuthNetwork();

    Prefs getPrefs();

    SessionCache getSessionCache();

    LocationsNetwork getLocationsNetwork();

    LocationsSupplier getLocationsSupplier();

    LocationDao getLocationDao();

    MapDao getMapDao();

    LocationDaoWorker provideLocationDatabaseWorker();

    MapDaoWorker provideMapDatabaseWorker();

    SavedLocationsSender provideSendSaveLocationsUseCase();
}



