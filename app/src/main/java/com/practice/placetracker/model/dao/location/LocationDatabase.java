package com.practice.placetracker.model.dao.location;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {TrackedLocationSchema.class}, version = 8, exportSchema = false)
public abstract class LocationDatabase extends RoomDatabase implements LocationDao {

    public static final String DB_NAME = "locations.db";

    public static LocationDatabase newInstance(Context context) {
        return Room.databaseBuilder(context, LocationDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public abstract LocationRoomDao locationDao();
}

