package com.practice.placetracker.model.data.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {TrackedLocation.class}, version = 3, exportSchema = false)
public abstract class LocationDatabase extends RoomDatabase {
    private static LocationDatabase database;
    private static final String DB_NAME = "locations.db";
    private static final Object LOCK = new Object();

    public static LocationDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (database == null) {
                database = Room.databaseBuilder(context, LocationDatabase.class, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return database;
        }
    }

    public abstract LocationDao locationDao();
}
