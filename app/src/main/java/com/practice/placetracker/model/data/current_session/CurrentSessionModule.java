package com.practice.placetracker.model.data.current_session;

import dagger.Module;
import dagger.Provides;

@Module
public class CurrentSessionModule {
    @Provides
    CurrentTrackingSession provideTrackingSession(){
        return CurrentTrackingSession.getInstance();
    }
}
