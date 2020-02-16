package com.practice.placetracker.ui.foreground_fragment;

public interface ForegroundContract {
    interface ForegroundBasePresenter{
        void showRegistrationFragment();
        void showLoginFragment();
    }
    interface ForegroundView{
        void openRegistrationFragment();
        void openLoginFragment();
    }
}
