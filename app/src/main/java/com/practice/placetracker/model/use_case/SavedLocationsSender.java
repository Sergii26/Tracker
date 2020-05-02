package com.practice.placetracker.model.use_case;

import com.practice.placetracker.model.network.Result;

import io.reactivex.Observable;

public interface SavedLocationsSender {
    Observable<Result<Boolean>> sendSavedLocations();
}
