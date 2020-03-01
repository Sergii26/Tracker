package com.practice.placetracker.model.location_api;

import android.content.Context;

import com.practice.placetracker.App;
import com.practice.placetracker.service.LocationServicePresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class LocationClientModule {

    @Provides
    ILocationClient provideLocationClient(){
        return new LocationClient(App.getInstance().getAppContext(), LocationServicePresenter.UPDATE_LOCATION_TIME,
                LocationServicePresenter.UPDATE_LOCATION_DISTANCE);
    }
}
