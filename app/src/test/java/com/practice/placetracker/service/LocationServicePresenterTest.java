package com.practice.placetracker.service;

import android.location.Location;

import com.practice.placetracker.model.cache.SessionCache;
import com.practice.placetracker.model.cache.SessionCacheImpl;
import com.practice.placetracker.model.dao.TrackedLocationSchema;
import com.practice.placetracker.model.dao.location.LocationDaoWorker;
import com.practice.placetracker.model.dao.location.LocationDatabaseWorker;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.model.network.Result;
import com.practice.placetracker.model.network.location.FirebaseLocationsNetwork;
import com.practice.placetracker.model.network.location.LocationsNetwork;
import com.practice.placetracker.model.prefs.Prefs;
import com.practice.placetracker.model.prefs.PrefsImpl;
import com.practice.placetracker.model.tracker.LocationClient;
import com.practice.placetracker.model.tracker.LocationsSupplier;
import com.practice.placetracker.model.tracker.TrackingResult;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

public class LocationServicePresenterTest {

    private LocationServiceContract.Presenter presenter;
    private LocationServiceContract.Service service;
    private LocationsSupplier locationsSupplier;
    private LocationDaoWorker dbWorker;
    private SessionCache sessionCache;
    private Prefs prefs;
    private LocationsNetwork network;
    private ILog logger;
    private Location location;

    @BeforeClass
    public static void setupClass() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                __ -> Schedulers.trampoline());
    }

    @Before
    public void initPresenter() {
        service = Mockito.mock(LocationServiceContract.Service.class);
        locationsSupplier = Mockito.mock(LocationsSupplier.class);
        dbWorker = Mockito.mock(LocationDaoWorker.class);
        sessionCache = Mockito.mock(SessionCache.class);
        prefs = Mockito.mock(Prefs.class);
        network = Mockito.mock(LocationsNetwork.class);
        logger = Mockito.mock(ILog.class);

        Mockito.when(dbWorker.insertLocation(Mockito.any())).thenReturn(Completable.fromSingle(Single.just("")));
        Mockito.when(prefs.getEmail()).thenReturn("");
        location = new Location("");
        location.setLatitude(1);
        location.setLongitude(1);
        Mockito.when(locationsSupplier.getLocationsObservable()).thenReturn(Observable.just(new TrackingResult(SessionCache.TYPE_TIME,
                location)));

        presenter = new LocationServicePresenter(service, locationsSupplier, dbWorker, sessionCache, prefs, network, logger);

    }

    @Test
    public void startTracking_withConnectionToNetworkAndFailedResult() {
        Mockito.when(service.isConnectedToNetwork()).thenReturn(true);
        Mockito.when(network.sendLocation(Mockito.any())).thenReturn(Single.just(new Result<>(new Exception())));
        presenter.startTracking();

        waitForInteraction();

        Mockito.verify(sessionCache).drop();
        Mockito.verify(locationsSupplier).getLocationsObservable();
        Mockito.verify(prefs).getEmail();
        Mockito.verify(sessionCache).updateSession(SessionCache.TYPE_TIME);
        Mockito.verify(service).isConnectedToNetwork();
        Mockito.verify(network).sendLocation(Mockito.any(TrackedLocationSchema.class));
        Mockito.verify(dbWorker).insertLocation(Mockito.any(TrackedLocationSchema.class));
        Mockito.verify(locationsSupplier).startLocationObserving();
        Mockito.verify(service).scheduleJob();
        verifyNoMore();
    }

    @Test
    public void startTracking_withConnectionToNetworkAndDataInResult() {
        Mockito.when(service.isConnectedToNetwork()).thenReturn(true);
        Mockito.when(network.sendLocation(Mockito.any())).thenReturn(Single.just(new Result<>(true)));
        presenter.startTracking();

        waitForInteraction();

        Mockito.verify(sessionCache).drop();
        Mockito.verify(locationsSupplier).getLocationsObservable();
        Mockito.verify(prefs).getEmail();
        Mockito.verify(sessionCache).updateSession(SessionCache.TYPE_TIME);
        Mockito.verify(service).isConnectedToNetwork();
        Mockito.verify(network).sendLocation(Mockito.any(TrackedLocationSchema.class));
        Mockito.verify(locationsSupplier).startLocationObserving();
        verifyNoMore();
    }

    @Test
    public void startTracking_withoutConnectionToNetwork() {
        Mockito.when(service.isConnectedToNetwork()).thenReturn(false);
        presenter.startTracking();

        waitForInteraction();

        Mockito.verify(sessionCache).drop();
        Mockito.verify(locationsSupplier).getLocationsObservable();
        Mockito.verify(prefs).getEmail();
        Mockito.verify(sessionCache).updateSession(SessionCache.TYPE_TIME);
        Mockito.verify(service).isConnectedToNetwork();
        Mockito.verify(dbWorker).insertLocation(Mockito.any(TrackedLocationSchema.class));
        Mockito.verify(service).scheduleJob();
        Mockito.verify(locationsSupplier).startLocationObserving();
        verifyNoMore();
    }

    private void waitForInteraction(){
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void verifyNoMore() {
        Mockito.verifyNoMoreInteractions(dbWorker);
        Mockito.verifyNoMoreInteractions(prefs);
        Mockito.verifyNoMoreInteractions(service);
        Mockito.verifyNoMoreInteractions(locationsSupplier);
        Mockito.verifyNoMoreInteractions(sessionCache);
        Mockito.verifyNoMoreInteractions(network);
    }

}
