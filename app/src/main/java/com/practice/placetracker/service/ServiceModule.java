package com.practice.placetracker.service;

import com.practice.placetracker.App;
import com.practice.placetracker.AppComponent;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    private LocationServiceContract.BaseLocationService service;

    public ServiceModule(LocationServiceContract.BaseLocationService service) {
        this.service = service;
    }

    @Provides
    public LocationServiceContract.BaseLocationServicePresenter providePresenter(){
        final AppComponent appComponent = App.getInstance().getAppComponent();
        return new LocationServicePresenter(service, appComponent.provideLocationClient(), appComponent.provideDatabaseWorker());

    }
}
