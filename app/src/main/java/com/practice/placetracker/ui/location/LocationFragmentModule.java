package com.practice.placetracker.ui.location;

import com.practice.placetracker.App;
import com.practice.placetracker.AppComponent;
import com.practice.placetracker.model.logger.Logger;

import dagger.Module;
import dagger.Provides;

@Module
public class LocationFragmentModule {
    private final AppComponent appComponent;

    LocationFragmentModule() {
        appComponent = App.getInstance().getAppComponent();
        appComponent.injectLocationFragment(this);
    }

    @Provides
    LocationContract.Presenter providePresenter() {
        return new LocationPresenter(appComponent.getSessionCache(),
                appComponent.getAuthNetwork(), Logger.withTag("MyLog"), appComponent.getPrefs());
    }
}

