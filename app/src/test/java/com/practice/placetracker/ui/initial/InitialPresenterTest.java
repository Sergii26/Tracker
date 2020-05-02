package com.practice.placetracker.ui.initial;

import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.model.prefs.Prefs;
import com.practice.placetracker.model.prefs.PrefsImpl;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

public class InitialPresenterTest {
    private ILog logger;
    private Prefs prefs;
    private InitialFragment view;
    private InitialContract.Presenter presenter;

    @BeforeClass
    public static void setupClass() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                __ -> Schedulers.trampoline());
    }

    @Before
    public void initPresenter() {
        logger = Mockito.mock(Logger.class);
        prefs = Mockito.mock(PrefsImpl.class);
        view = Mockito.mock(InitialFragment.class);
        presenter = new InitialPresenter(prefs, logger);
        presenter.subscribe(view);
    }

    @Test
    public void checkAuth_AlreadySignIn(){
        Mockito.when(prefs.getEmail()).thenReturn("test");
        Mockito.when(view.hasCallBack()).thenReturn(true);
        presenter.checkAuth();

        try {
            Thread.sleep(550);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Mockito.verify(prefs, Mockito.times(2)).getEmail();
        Mockito.verify(view).goToMainScreen();
        verifyNoMore();
    }

    @Test
    public void checkAuth_NotSignIn(){
        Mockito.when(prefs.getEmail()).thenReturn("");
        Mockito.when(view.hasCallBack()).thenReturn(true);
        presenter.checkAuth();

        try {
            Thread.sleep(550);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Mockito.verify(prefs, Mockito.times(2)).getEmail();
        Mockito.verify(view).enableButtons();
        verifyNoMore();
    }

    private void verifyNoMore() {
        Mockito.verifyNoMoreInteractions(prefs);
        Mockito.verifyNoMoreInteractions(view);
    }
}
