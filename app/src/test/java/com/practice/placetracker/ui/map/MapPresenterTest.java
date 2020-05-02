package com.practice.placetracker.ui.map;

import com.google.common.base.Optional;
import com.practice.placetracker.model.dao.TrackedLocationSchema;
import com.practice.placetracker.model.dao.map.MapDaoWorker;
import com.practice.placetracker.model.dao.map.MapDatabaseWorker;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.model.network.Result;
import com.practice.placetracker.model.network.auth.AuthNetwork;
import com.practice.placetracker.model.network.auth.FirebaseAuthNetwork;
import com.practice.placetracker.model.network.location.FirebaseLocationsNetwork;
import com.practice.placetracker.model.network.location.LocationsNetwork;
import com.practice.placetracker.model.prefs.Prefs;
import com.practice.placetracker.model.prefs.PrefsImpl;
import com.practice.placetracker.ui.map.map.MapFragment;
import com.practice.placetracker.ui.map.map.MapPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MapPresenterTest {

    private ILog logger;
    private AuthNetwork authNetwork;
    private LocationsNetwork locationNetwork;
    private Prefs prefs;
    private MapDaoWorker dbWorker;
    private MapPresenter presenter;
    private MapFragment view;

    @BeforeClass
    public static void setupClass() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                __ -> Schedulers.trampoline());
    }

    @Before
    public void setupPresenter(){
        logger = mock(ILog.class);
        authNetwork = mock(AuthNetwork.class);
        locationNetwork = mock(LocationsNetwork.class);
        prefs = mock(Prefs.class);
        dbWorker = mock(MapDaoWorker.class);
        view = mock(MapFragment.class);
        presenter = new MapPresenter(authNetwork, locationNetwork, prefs, dbWorker, logger);
        presenter.subscribe(view);
    }

    @After
    public void clearPresenter(){
        presenter.unsubscribe();
    }

    @Test
    public void showLocationsTest_databaseWithLocations(){
        List<TrackedLocationSchema> locationsForMockedObjects = new ArrayList<>();
        TrackedLocationSchema location =
                new TrackedLocationSchema(0, 0, 0, 1, 0, false, "");
        locationsForMockedObjects.add(location);
        when(view.isConnectedToNetwork()).thenReturn(true);
        when(locationNetwork.subscribeToFirestoreChanges(Mockito.anyString()))
                .thenReturn(BehaviorSubject.createDefault(0));
        when(dbWorker.getLastLocation(Mockito.anyString()))
                .thenReturn((Single.just(Optional.of(new TrackedLocationSchema(0,
                        0, 0, 0, 0, false, "")))));
        when(locationNetwork.getLocationsWhereTimeGreaterThen(Mockito.anyString(), Mockito.any(Long.class)))
                .thenReturn(Single.just(new Result<List<TrackedLocationSchema>>(locationsForMockedObjects)));
        when(dbWorker.insertLocation(Mockito.any())).thenReturn(Completable.complete());
        when(prefs.getEmail()).thenReturn("");
        when(dbWorker.getAllUserLocations(Mockito.anyString())).thenReturn(Single.just(locationsForMockedObjects));
        presenter.showLocations();

        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        verify(view).isConnectedToNetwork();
        verify(prefs, Mockito.times(4)).getEmail();
        verify(dbWorker).getAllUserLocations(Mockito.anyString());
        verify(view, Mockito.times(2)).addPolylines(Mockito.anyList());
        verify(view, Mockito.times(2)).setCameraPosition(Mockito.any(TrackedLocationSchema.class), Mockito.any(Integer.class));
        verify(locationNetwork).subscribeToFirestoreChanges(Mockito.anyString());
        verify(dbWorker).getLastLocation(Mockito.anyString());
        verify(locationNetwork).getLocationsWhereTimeGreaterThen(Mockito.anyString(), Mockito.any(Long.class));
        verify(dbWorker).insertLocation(Mockito.any());
        verifyNoMore();
    }

    @Test
    public void showLocationsTest_withEmptyDatabase(){
        List<TrackedLocationSchema> locationsForMockedObjects = new ArrayList<>();
        TrackedLocationSchema location =
                new TrackedLocationSchema(0, 0, 0, 1, 0, false, "");
        locationsForMockedObjects.add(location);
        when(view.isConnectedToNetwork()).thenReturn(true);
        when(locationNetwork.subscribeToFirestoreChanges(Mockito.anyString()))
                .thenReturn(BehaviorSubject.createDefault(0));
        when(dbWorker.getLastLocation(Mockito.anyString()))
                .thenReturn((Single.just(Optional.absent())));
        when(locationNetwork.getLocationsWhereTimeGreaterThen(Mockito.anyString(), Mockito.any(Long.class)))
                .thenReturn(Single.just(new Result<List<TrackedLocationSchema>>(locationsForMockedObjects)));
        when(dbWorker.insertLocation(Mockito.any())).thenReturn(Completable.complete());
        when(prefs.getEmail()).thenReturn("");
        when(dbWorker.getAllUserLocations(Mockito.anyString())).thenReturn(Single.just(new ArrayList<>()));
        presenter.showLocations();

        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        verify(view).isConnectedToNetwork();
        verify(prefs, Mockito.times(4)).getEmail();
        verify(dbWorker).getAllUserLocations(Mockito.anyString());
        verify(view, Mockito.times(1)).addPolylines(Mockito.anyList());
        verify(view, Mockito.times(1)).setCameraPosition(Mockito.any(TrackedLocationSchema.class), Mockito.any(Integer.class));
        verify(locationNetwork).subscribeToFirestoreChanges(Mockito.anyString());
        verify(view).showToast(Mockito.any(Integer.class));
        verify(dbWorker).getLastLocation(Mockito.anyString());
        verify(locationNetwork).getLocationsWhereTimeGreaterThen(Mockito.anyString(), Mockito.any(Long.class));
        verify(dbWorker).insertLocation(Mockito.any());
        verifyNoMore();
    }



    private void verifyNoMore(){
        verifyNoMoreInteractions(authNetwork);
        verifyNoMoreInteractions(locationNetwork);
        verifyNoMoreInteractions(prefs);
        verifyNoMoreInteractions(dbWorker);
        verifyNoMoreInteractions(view);
    }
}
