package com.practice.placetracker.ui.auth;

import com.practice.placetracker.App;
import com.practice.placetracker.AppComponent;

public class AuthFragmentInjector {
    static AuthPresenter injectPresenter(int mode) {
        final AppComponent appComponent = App.getInstance().getAppComponent();
        return new AuthPresenter(appComponent.getAuthNetwork(), appComponent.getPrefs(), mode);
    }
}

