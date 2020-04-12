package com.practice.placetracker.service;

import dagger.Component;

@Component(modules = {ServiceModule.class})
public interface ServiceComponent {
    void injectLocationService(LocationService service);
}

