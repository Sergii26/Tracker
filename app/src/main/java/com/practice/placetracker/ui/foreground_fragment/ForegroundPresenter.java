package com.practice.placetracker.ui.foreground_fragment;

public class ForegroundPresenter implements ForegroundContract.ForegroundBasePresenter {

    private ForegroundContract.ForegroundView view;

    ForegroundPresenter(ForegroundContract.ForegroundView view){
        this.view = view;
    }

    @Override
    public void showRegistrationFragment() {
        view.openRegistrationFragment();
    }

    @Override
    public void showLoginFragment() {
        view.openLoginFragment();
    }
}
