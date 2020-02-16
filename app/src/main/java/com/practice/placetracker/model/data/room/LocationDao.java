package com.practice.placetracker.model.data.room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Completable;
import io.reactivex.Flowable;


@Dao
public interface LocationDao {

    @Query("SELECT * FROM locations")
    List<TrackedLocation> getAllLocations();

    @Query("SELECT * FROM locations WHERE isSent = :isSent")
    List<TrackedLocation> getLocationsBySent(boolean isSent);

    @Query("SELECT * FROM locations")
    Flowable<List<TrackedLocation>> getAllLocationsRx();

    @Query("DELETE FROM locations")
    Completable deleteAllLocations();

    @Insert
    Completable insertLocation(TrackedLocation location);

    @Delete
    Completable deleteLocation(TrackedLocation trackedLocation);

//    @Query("UPDATE orders SET order_price=:price WHERE order_id = :id")
//    void update(Float price, int id);

    @Query("UPDATE locations SET isSent=:isSent WHERE uniqueId = :uniqueId")
    Completable update(boolean isSent, long uniqueId);

    @Query("DELETE from locations WHERE isSent = :isSent")
    Completable deleteSentLocations(boolean isSent);


}
