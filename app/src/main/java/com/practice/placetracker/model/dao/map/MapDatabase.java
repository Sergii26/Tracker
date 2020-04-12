package com.practice.placetracker.model.dao.map;

import android.content.Context;

import com.practice.placetracker.model.dao.TrackedLocationSchema;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TrackedLocationSchema.class}, version = 3, exportSchema = false)
public abstract class MapDatabase extends RoomDatabase  implements MapDao{

    private static final ILog logger = Logger.withTag("MyLog");

    public static final String DB_NAME = "locations.db";

    public static MapDatabase newInstance(Context context) {
        logger.log("MapDatabase newInstance");
        return Room.databaseBuilder(context, MapDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public abstract MapRoomDao mapDao();
}