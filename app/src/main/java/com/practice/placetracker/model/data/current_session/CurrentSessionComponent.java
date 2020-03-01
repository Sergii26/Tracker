package com.practice.placetracker.model.data.current_session;

import dagger.Component;

@Component(modules = {CurrentSessionModule.class})
public interface CurrentSessionComponent {
    CurrentTrackingSession provideCurrentTrackingSession();
}
