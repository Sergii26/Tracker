package com.practice.placetracker.model.location_api;

import dagger.Component;

@Component(modules = {LocationClientModule.class})
public interface LocationClientComponent {
    ILocationClient provideLocationClient();
}
