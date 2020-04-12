package com.practice.placetracker.model.network.location;

import com.practice.placetracker.model.dao.location.TrackedLocationSchema;
import com.practice.placetracker.model.network.Result;

import io.reactivex.Single;

public interface LocationsNetwork {

    Single<Result<Boolean>> sendLocation(TrackedLocationSchema location);

}
