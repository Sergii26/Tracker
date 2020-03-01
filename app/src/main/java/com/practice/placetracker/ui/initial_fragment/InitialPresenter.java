package com.practice.placetracker.ui.initial_fragment;

public class InitialPresenter implements InitialContract.Presenter {

    private InitialContract.View view;

    InitialPresenter(InitialContract.View view){
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
