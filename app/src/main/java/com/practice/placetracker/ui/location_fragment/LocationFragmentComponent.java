package com.practice.placetracker.ui.location_fragment;

import dagger.Component;

@Component(modules = {LocationFragmentModule.class})
public interface LocationFragmentComponent {
    void injectLocationFragment(LocationFragment fragment);
}
