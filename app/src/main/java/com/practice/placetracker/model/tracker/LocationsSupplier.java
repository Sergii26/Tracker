package com.practice.placetracker.model.tracker;

import io.reactivex.Observable;

public interface LocationsSupplier {

    void startLocationObserving();

    void stopLocationObserving();

    Observable<TrackingResult> getLocationsObservable();
}

