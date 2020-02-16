package com.practice.placetracker.service.job_service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.practice.placetracker.model.data.room.DatabaseWorker;
import com.practice.placetracker.model.data.room.LocationDatabase;
import com.practice.placetracker.model.data.room.TrackedLocation;
import com.practice.placetracker.model.firestore_api.FirestoreClient;

import java.util.List;

import androidx.annotation.Nullable;

public class CloudDatabaseIntentService extends IntentService {

    public static final String ACTION_SEND_TO_DATABASE = "com.practice.placetracker.action.SEND_TO_DATABASE";

    public CloudDatabaseIntentService() {
        super("CloudDatabaseIntentService");
    }

    public static void startActionSendToDatabase(Context context) {
        Log.i("MyLog", "CloudDatabaseIntentService in startActionSendToDatabase");
        Intent intent = new Intent(context, CloudDatabaseIntentService.class);
        intent.setAction(ACTION_SEND_TO_DATABASE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i("MyLog", "CloudDatabaseIntentService in onHandleIntent");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SEND_TO_DATABASE.equals(action)) {
                handleActionSendToDatabase();
            }
        }
    }

    private void handleActionSendToDatabase() {
        Log.i("MyLog", "CloudDatabaseIntentService in handleActionSendToDatabase");
        DatabaseWorker dbWorker = new DatabaseWorker(this);
        dbWorker.showSizeInLog();
        FirestoreClient firestoreClient = new FirestoreClient();
        List<TrackedLocation> locations = LocationDatabase.getInstance(this).locationDao().getLocationsBySent(false);
        for(TrackedLocation location: locations){
            firestoreClient.sendLocationToServer(location);
            dbWorker.updateLocation(true, location.getUniqueId());
        }
    }

}
