package com.practice.placetracker.ui.tracker.location;

import dagger.Component;

@Component(modules = {LocationFragmentModule.class})
public interface LocationFragmentComponent {
    void injectLocationFragment(LocationFragment fragment);
}

