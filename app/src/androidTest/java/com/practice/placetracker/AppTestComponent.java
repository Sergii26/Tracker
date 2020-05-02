package com.practice.placetracker;

import com.practice.placetracker.model.cache.SessionCache;
import com.practice.placetracker.model.dao.location.LocationDao;
import com.practice.placetracker.model.dao.location.LocationDaoWorker;
import com.practice.placetracker.model.dao.map.MapDao;
import com.practice.placetracker.model.dao.map.MapDaoWorker;
import com.practice.placetracker.model.network.Result;
import com.practice.placetracker.model.network.auth.AuthNetwork;
import com.practice.placetracker.model.network.location.LocationsNetwork;
import com.practice.placetracker.model.prefs.Prefs;
import com.practice.placetracker.model.tracker.LocationsSupplier;
import com.practice.placetracker.model.use_case.UseCase;
import com.practice.placetracker.ui.map.map.MapFragmentModule;
import com.practice.placetracker.ui.tracker.location.LocationFragmentModule;

import javax.inject.Singleton;

import dagger.Component;
import io.reactivex.Observable;

@Singleton
@Component(modules = {AppTestModule.class})
public interface AppTestComponent extends AppComponent{
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

    UseCase<Void, Observable<Result<Boolean>>> provideSendSaveLocationsUseCase();
}
