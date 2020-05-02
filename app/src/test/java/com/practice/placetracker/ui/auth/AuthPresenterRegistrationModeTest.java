package com.practice.placetracker.ui.auth;

import android.content.Context;

import com.practice.placetracker.R;
import com.practice.placetracker.model.network.Result;
import com.practice.placetracker.model.network.auth.AuthNetwork;
import com.practice.placetracker.model.network.auth.FirebaseAuthNetwork;
import com.practice.placetracker.model.prefs.Prefs;
import com.practice.placetracker.model.prefs.PrefsImpl;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.eq;

public class AuthPresenterRegistrationModeTest {
    private final int mode = FragmentIndication.REGISTRATION_MODE;
    private AuthNetwork network;
    private Prefs prefs;
    private AuthPresenter presenter;
    private AuthContract.View view;
    private Context ctx;


    @BeforeClass
    public static void setupClass() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                __ -> Schedulers.trampoline());
    }

    @Before
    public void initPresenter() {
        network = Mockito.mock(FirebaseAuthNetwork.class);
        prefs = Mockito.mock(PrefsImpl.class);
        view = Mockito.mock(AuthFragment.class);
        presenter = new AuthPresenter(network, prefs, mode);
        presenter.subscribe(view);

    }

    @Test
    public void onButtonClick_testLoginWithResultWithData() {
        Mockito.when(network.registerNewUser("", ""))
                .thenReturn(Single.just(new Result<>(true)));
        Mockito.when(view.getString(R.string.dialog_message)).thenReturn("Sign in");
        presenter.onButtonClick("", "");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Mockito.verify(network).registerNewUser("", "");
        Mockito.verify(view).getString(R.string.dialog_message);
        Mockito.verify(view).showProgress(Mockito.anyString(), eq("Sign in"));
        Mockito.verify(view).hideProgress();
        Mockito.verify(prefs).putEmail("");
        Mockito.verify(view).openLocationFragment();
        verifyNoMore();
    }

    @Test
    public void onButtonClick_testLoginWithFailedResult() {
        Mockito.when(network.registerNewUser("", ""))
                .thenReturn(Single.just(new Result<>(new Exception())));
        Mockito.when(view.getString(R.string.dialog_message)).thenReturn("Sign in");
        presenter.onButtonClick("", "");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Mockito.verify(network).registerNewUser("", "");
        Mockito.verify(view).getString(R.string.dialog_message);
        Mockito.verify(view).showProgress(Mockito.anyString(), eq("Sign in"));
        Mockito.verify(view).hideProgress();
        Mockito.verify(view).showToast(R.string.sign_in_or_registration_error);
        verifyNoMore();
    }

    @Test
    public void setupSubscription_withValidLoginAndPassword(){

        Mockito.when(view.getPasswordObservable()).thenReturn(Observable.just("password"));
        Mockito.when(view.getEmailObservable()).thenReturn(Observable.just("email"));
        Mockito.when(view.getString(Mockito.any(Integer.class))).thenReturn(Mockito.anyString());
        presenter.setupSubscriptions();

        Mockito.verify(view).getString(R.string.label_registration);
        Mockito.verify(view).setButtonText(Mockito.anyString());
        Mockito.verify(view).getPasswordObservable();
        Mockito.verify(view).setPasswordError(null);
        Mockito.verify(view).getEmailObservable();
        Mockito.verify(view).setUsernameError(null);
        Mockito.verify(view).sentRequestButtonEnabled(true);
        verifyNoMore();

    }

    @Test
    public void setupSubscription_withInvalidPasswordAndValidEmail(){

        Mockito.when(view.getPasswordObservable()).thenReturn(Observable.just("pas"));
        Mockito.when(view.getEmailObservable()).thenReturn(Observable.just("email"));
        Mockito.when(view.getString(Mockito.any(Integer.class))).thenReturn(Mockito.anyString());
        presenter.setupSubscriptions();

        Mockito.verify(view).getString(R.string.label_registration);
        Mockito.verify(view).setButtonText(Mockito.anyString());
        Mockito.verify(view).getPasswordObservable();
        Mockito.verify(view).getString(R.string.password_too_short);
        Mockito.verify(view).setPasswordError(Mockito.anyString());
        Mockito.verify(view).getEmailObservable();
        Mockito.verify(view).setUsernameError(null);
        Mockito.verify(view).sentRequestButtonEnabled(false);
        verifyNoMore();
    }

    @Test
    public void setupSubscription_withValidPasswordAndEmptyEmail(){

        Mockito.when(view.getPasswordObservable()).thenReturn(Observable.just("password"));
        Mockito.when(view.getEmailObservable()).thenReturn(Observable.just(""));
        Mockito.when(view.getString(Mockito.any(Integer.class))).thenReturn(Mockito.anyString());
        presenter.setupSubscriptions();

        Mockito.verify(view).getString(R.string.label_registration);
        Mockito.verify(view).setButtonText(Mockito.anyString());
        Mockito.verify(view).getPasswordObservable();
        Mockito.verify(view).setPasswordError(null);
        Mockito.verify(view).getEmailObservable();
        Mockito.verify(view).getString(R.string.username_is_empty);
        Mockito.verify(view).setUsernameError(Mockito.anyString());
        Mockito.verify(view).sentRequestButtonEnabled(false);
        verifyNoMore();
    }

    @Test
    public void setupSubscription_withInvalidPasswordAndEmptyEmail(){

        Mockito.when(view.getPasswordObservable()).thenReturn(Observable.just("pas"));
        Mockito.when(view.getEmailObservable()).thenReturn(Observable.just(""));
        Mockito.when(view.getString(Mockito.any(Integer.class))).thenReturn(Mockito.anyString());
        presenter.setupSubscriptions();

        Mockito.verify(view).getString(R.string.label_registration);
        Mockito.verify(view).setButtonText(Mockito.anyString());
        Mockito.verify(view).getPasswordObservable();
        Mockito.verify(view).getString(R.string.password_too_short);
        Mockito.verify(view).setPasswordError(Mockito.anyString());
        Mockito.verify(view).getEmailObservable();
        Mockito.verify(view).getString(R.string.username_is_empty);
        Mockito.verify(view).setUsernameError(Mockito.anyString());
        Mockito.verify(view).sentRequestButtonEnabled(false);
        verifyNoMore();
    }

    private void verifyNoMore() {
        Mockito.verifyNoMoreInteractions(network);
        Mockito.verifyNoMoreInteractions(view);
        Mockito.verifyNoMoreInteractions(prefs);
    }
}
