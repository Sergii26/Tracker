package com.practice.placetracker.service;

import com.practice.placetracker.App;
import com.practice.placetracker.AppComponent;
import com.practice.placetracker.model.dao.location.DatabaseWorker;
import com.practice.placetracker.model.logger.Logger;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    private LocationServiceContract.Service service;

    ServiceModule(LocationServiceContract.Service service) {
        this.service = service;
    }

    @Provides
    public LocationServiceContract.Presenter providePresenter() {
        final AppComponent appComponent = App.getInstance().getAppComponent();
        return new LocationServicePresenter(service, appComponent.getLocationsSupplier(),
                new DatabaseWorker(appComponent.getLocationDao(), Logger.withTag("MyLog")),
                appComponent.getSessionCache(), appComponent.getPrefs(), appComponent.getLocationsNetwork());
    }
}

