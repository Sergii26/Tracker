package com.practice.placetracker.ui.initial_fragment;

public interface InitialContract {
    interface Presenter {
        void showRegistrationFragment();
        void showLoginFragment();
    }
    interface View {
        void openRegistrationFragment();
        void openLoginFragment();
    }
}
