package com.practice.placetracker.model.dao.location;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Completable;


@Dao
public interface LocationRoomDao {

    @Query("SELECT * FROM locations")
    List<TrackedLocationSchema> getAllLocations();

    @Query("SELECT * FROM locations WHERE isSent = :isSent")
    List<TrackedLocationSchema> getLocationsBySent(boolean isSent);

    @Insert
    Completable insertLocation(TrackedLocationSchema location);

    @Delete
    Completable deleteLocation(TrackedLocationSchema trackedLocation);

}

