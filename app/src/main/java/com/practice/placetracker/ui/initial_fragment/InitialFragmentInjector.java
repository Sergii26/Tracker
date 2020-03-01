package com.practice.placetracker.ui.initial_fragment;

public class InitialFragmentInjector {
    static InitialContract.Presenter injectPresenter(InitialContract.View view){
        return new InitialPresenter(view);
    }
}
