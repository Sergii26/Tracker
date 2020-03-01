package com.practice.placetracker.model.data.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {TrackedLocation.class}, version = 7, exportSchema = false)
public abstract class LocationDatabase extends RoomDatabase implements IDatabase {

    public static final String DB_NAME = "locations.db";

    public static LocationDatabase getInstance(Context context) {

        return Room.databaseBuilder(context, LocationDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public abstract LocationDao locationDao();
}
