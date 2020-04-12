package com.practice.placetracker.ui.initial;

import com.practice.placetracker.ui.arch.Contract;

public interface InitialContract {

    interface Presenter extends Contract.Presenter<View> {
        void checkAuth();
    }

    interface View extends Contract.View {
        void enableButtons();

        void goToMainScreen();
    }

    interface Host extends Contract.Host {
        void showLoginFragment();

        void showRegistrationFragment();

        void showLocationFragment();
    }
}

