package com.practice.placetracker.service.job_service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.practice.placetracker.android_utils.ILog;
import com.practice.placetracker.android_utils.Logger;
import com.practice.placetracker.model.data.room.DaggerDatabaseWorkerComponent;
import com.practice.placetracker.model.data.room.DatabaseWorker;
import com.practice.placetracker.model.data.room.DatabaseWorkerComponent;
import com.practice.placetracker.model.data.room.TrackedLocation;
import com.practice.placetracker.model.firestore_api.FirestoreClient;
import com.practice.placetracker.model.firestore_api.IFirestoreClient;

import java.util.List;

import androidx.annotation.Nullable;

public class ScheduledSanderService extends IntentService {

    private static final ILog logger = Logger.withTag("MyLog");

    public static final String ACTION_SEND_TO_DATABASE = "com.practice.placetracker.action.SEND_TO_DATABASE";

    public ScheduledSanderService() {
        super("ScheduledSanderService");
    }

    public static void startActionSendToDatabase(Context context) {
        logger.log("ScheduledSanderService in startActionSendToDatabase");
        Intent intent = new Intent(context, ScheduledSanderService.class);
        intent.setAction(ACTION_SEND_TO_DATABASE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        logger.log("ScheduledSanderService in onHandleIntent");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SEND_TO_DATABASE.equals(action)) {
                handleActionSendToDatabase();
            }
        }
    }

    private void handleActionSendToDatabase() {
        logger.log("ScheduledSanderService in handleActionSendToDatabase");
        DatabaseWorkerComponent dbWorkerComponent = DaggerDatabaseWorkerComponent.create();
        DatabaseWorker dbWorker = dbWorkerComponent.provideDatabaseWorker();
        List<TrackedLocation> locations = dbWorker.getLocationsBySent(false);
        if (locations.size() > 0) {
            dbWorker.showSizeInLog();
            IFirestoreClient firestoreClient = new FirestoreClient();
            for (TrackedLocation location : locations) {
                firestoreClient.sendLocationToServer(location, o -> {
                            logger.log("ScheduledSanderService in handleActionSendToDatabase onSuccess");
                            dbWorker.deleteLocation(location);
                        },
                        e -> {
                            logger.log("ScheduledSanderService in handleActionSendToDatabase onFailure msg: " + e.getMessage());
                        });
            }
        }
    }
}
