package com.practice.placetracker.model.dao.map;

import com.practice.placetracker.model.dao.TrackedLocationSchema;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Completable;

@Dao
public interface MapRoomDao {
    @Query("SELECT * FROM locations WHERE userEmail = :userEmail ORDER BY time")
    List<TrackedLocationSchema> getAllUserLocations(String userEmail);

    @Insert
    Completable insertLocation(List<TrackedLocationSchema> locations);

    @Query("SELECT * FROM locations WHERE userEmail = :userEmail ORDER BY time DESC LIMIT 1")
    List<TrackedLocationSchema> getLastLocation(String userEmail);

}
