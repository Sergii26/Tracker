package com.practice.placetracker.ui.map.map;

import com.practice.placetracker.ui.tracker.location.LocationFragment;

import dagger.Component;
import dagger.Module;

@Component(modules = {MapFragmentModule.class})
public interface MapFragmentComponent {
    void injectMapFragment(MapFragment fragment);
}
