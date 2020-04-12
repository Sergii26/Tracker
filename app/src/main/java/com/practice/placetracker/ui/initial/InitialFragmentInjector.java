package com.practice.placetracker.ui.initial;

import com.practice.placetracker.App;

class InitialFragmentInjector {

    static InitialContract.Presenter injectPresenter() {
        return new InitialPresenter(App.getInstance().getAppComponent().getPrefs());
    }
}

