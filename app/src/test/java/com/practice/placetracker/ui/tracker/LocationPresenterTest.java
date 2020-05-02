package com.practice.placetracker.ui.tracker;

import com.practice.placetracker.model.cache.SessionCache;
import com.practice.placetracker.model.cache.SessionCacheImpl;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.model.network.auth.AuthNetwork;
import com.practice.placetracker.model.network.auth.FirebaseAuthNetwork;
import com.practice.placetracker.model.prefs.Prefs;
import com.practice.placetracker.model.prefs.PrefsImpl;
import com.practice.placetracker.ui.tracker.location.LocationContract;
import com.practice.placetracker.ui.tracker.location.LocationFragment;
import com.practice.placetracker.ui.tracker.location.LocationPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class LocationPresenterTest {

    private ILog logger;
    private AuthNetwork authNetwork;
    private SessionCache sessionCache;
    private Prefs prefs;
    private LocationFragment view;
    private LocationContract.Presenter presenter;

    @BeforeClass
    public static void setupClass() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                __ -> Schedulers.trampoline());
    }

    @Before
    public void setupPresenter(){
        logger = mock(ILog.class);
        authNetwork = mock(AuthNetwork.class);
        sessionCache = mock(SessionCache.class);
        prefs = mock(Prefs.class);
        view = mock(LocationFragment.class);

        presenter = new LocationPresenter(sessionCache, authNetwork, logger, prefs);
        presenter.subscribe(view);
    }

    @After
    public void clearPresenter(){
        presenter.unsubscribe();
    }

    @Test
    public void startLocationTrackingTest_permissionsGranted(){
        when(view.isGrantedPermission()).thenReturn(true);
        when(sessionCache.getStartTime()).thenReturn(0L);
        presenter.startLocationTracking();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verify(view).isGrantedPermission();
        verify(view).setButtonsState(true);
        verify(view).startService();
        verify(sessionCache).setIsTracking(true);
        verify(sessionCache).putStartTime(Mockito.any(Long.class));
        verify(sessionCache, Mockito.times(2)).getStartTime();
        verify(view).setTime(Mockito.anyString());
        verifyNoMore();

    }

    @Test
    public void startLocationTrackingTest_withoutPermissions(){
        when(view.isGrantedPermission()).thenReturn(false);
        presenter.startLocationTracking();
        verify(view).isGrantedPermission();
        verify(view).requestPermission();
        verifyNoMore();

    }

    @Test
    public void stopLocationTrackingTest(){
        presenter.stopLocationTracking();
        verify(view).setButtonsState(false);
        verify(view).stopService();
        verify(sessionCache).setIsTracking(false);
        verify(sessionCache).putStartTime(0L);
        verifyNoMore();
    }

    @Test
    public void setupUiObservablesTest_isTracking(){
        when(sessionCache.getTrackingStateObservable()).thenReturn(Observable.just(true));
        when(sessionCache.getLocationsCountObservable())
                .thenReturn(Observable.just(1));
        when(sessionCache.getStartTime()).thenReturn(0L);
        presenter.setupUiObservables();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verify(sessionCache).getTrackingStateObservable();
        verify(view).setButtonsState(true);
        verify(sessionCache).getLocationsCountObservable();
        verify(view).updateTrackingInformation(Mockito.any(SessionCache.class));
        verify(sessionCache).putStartTime(Mockito.any(Long.class));
        verify(sessionCache, Mockito.times(2)).getStartTime();
        verify(view).setTime(Mockito.anyString());
        verifyNoMore();
    }

    @Test
    public void setupUiObservablesTest_notTracking(){
        when(sessionCache.getTrackingStateObservable()).thenReturn(Observable.just(false));
        when(sessionCache.getLocationsCountObservable())
                .thenReturn(Observable.just(1));
        when(sessionCache.getStartTime()).thenReturn(0L);
        presenter.setupUiObservables();

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verify(sessionCache).getTrackingStateObservable();
        verify(view).setButtonsState(false);
        verify(sessionCache).getLocationsCountObservable();
        verify(view).updateTrackingInformation(Mockito.any(SessionCache.class));
        verifyNoMore();
    }

    @Test
    public void logOutTest(){
        presenter.logOut();
        verify(prefs).putEmail("");
        verify(view).stopService();
        verify(sessionCache).drop();
        verify(sessionCache).putStartTime(0);
        verify(authNetwork).logOut();
        verifyNoMore();
    }

    private void verifyNoMore(){
        verifyNoMoreInteractions(authNetwork);
        verifyNoMoreInteractions(sessionCache);
        verifyNoMoreInteractions(prefs);
        verifyNoMoreInteractions(view);
    }
}
