package com.practice.placetracker.ui.map.map;

import com.practice.placetracker.App;
import com.practice.placetracker.AppComponent;
import com.practice.placetracker.model.logger.Logger;

import dagger.Module;
import dagger.Provides;

@Module
public class MapFragmentModule {
    private final AppComponent appComponent;

    MapFragmentModule() {
        appComponent = App.getInstance().getAppComponent();
        appComponent.injectMapFragment(this);
    }

    @Provides
    MapContract.Presenter provideMapPresenter(){
        return new MapPresenter(appComponent.getAuthNetwork(), appComponent.getLocationsNetwork(),
                appComponent.getPrefs(), appComponent.provideMapDatabaseWorker(), Logger.withTag("MyLog"));
    }
}
