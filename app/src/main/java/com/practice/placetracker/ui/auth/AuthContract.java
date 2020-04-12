package com.practice.placetracker.ui.auth;

import com.practice.placetracker.ui.arch.Contract;

import io.reactivex.Observable;

public class AuthContract {

    interface View extends Contract.View {

        void openLocationFragment();

        Observable<String> getPasswordObservable();

        Observable<String> getEmailObservable();

        void setPasswordError(String errorMsg);

        void setUsernameError(String errorMsg);

        void sentRequestButtonEnabled(Boolean isEnabled);

        void setButtonText(String txt);
    }

    interface Presenter extends Contract.Presenter<View> {

        void onButtonClick(String email, String password);

        void setupSubscriptions();
    }

    public interface Host extends Contract.Host {

        void showLocationFragment();
    }

}