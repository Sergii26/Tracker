package com.practice.placetracker.model.dao.location;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Flowable;


@Dao
public interface LocationRoomDao {

    @Query("SELECT * FROM locations")
    List<TrackedLocationSchema> getAllLocations();

    @Query("SELECT * FROM locations WHERE isSent = :isSent")
    List<TrackedLocationSchema> getLocationsBySent(boolean isSent);

    @Query("SELECT * FROM locations")
    Flowable<List<TrackedLocationSchema>> getAllLocationsRx();

    @Query("DELETE FROM locations")
    Completable deleteAllLocations();

    @Insert
    Completable insertLocation(TrackedLocationSchema location);

    @Delete
    Completable deleteLocation(TrackedLocationSchema trackedLocation);

    @Query("UPDATE locations SET isSent=:isSent WHERE uniqueId = :uniqueId")
    Completable update(boolean isSent, long uniqueId);

    @Query("DELETE from locations WHERE uniqueId = :uniqueId")
    Completable deleteLocationById(long uniqueId);

    @Query("DELETE from locations WHERE isSent = :isSent")
    Completable deleteSentLocations(boolean isSent);

}

