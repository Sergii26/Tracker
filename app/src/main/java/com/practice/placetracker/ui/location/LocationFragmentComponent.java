package com.practice.placetracker.ui.location;

import dagger.Component;

@Component(modules = {LocationFragmentModule.class})
public interface LocationFragmentComponent {
    void injectLocationFragment(LocationFragment fragment);
}

