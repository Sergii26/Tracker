package com.practice.placetracker.service.job_service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class CloudDatabaseJobService extends JobService {

    public CloudDatabaseJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("MyLog", "CloudDatabaseJobService in onStartJob");
        CloudDatabaseIntentService.startActionSendToDatabase(getApplicationContext());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i("MyLog", "CloudDatabaseJobService in onStopJob");
        return false;
    }
}
