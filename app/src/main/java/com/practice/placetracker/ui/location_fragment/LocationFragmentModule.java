package com.practice.placetracker.ui.location_fragment;

import com.practice.placetracker.App;
import com.practice.placetracker.model.data.current_session.CurrentSessionComponent;
import com.practice.placetracker.model.data.current_session.DaggerCurrentSessionComponent;
import com.practice.placetracker.model.data.room.IDatabase;
import com.practice.placetracker.model.data.room.LocationDatabase;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

@Module
public class LocationFragmentModule {
    @Inject
    IDatabase database;

    private LocationContract.View view;

    LocationFragmentModule(LocationContract.View view){
        this.view = view;
        App.getInstance().getAppComponent().injectLocationFragment(this);
    }

    @Provides
    LocationContract.Presenter providePresenter(){
        final CurrentSessionComponent sessionComponent = DaggerCurrentSessionComponent.create();
        return new LocationPresenter(view, database, sessionComponent.provideCurrentTrackingSession());
    }
}
