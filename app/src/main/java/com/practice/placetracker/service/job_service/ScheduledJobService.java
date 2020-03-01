package com.practice.placetracker.service.job_service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.practice.placetracker.android_utils.ILog;
import com.practice.placetracker.android_utils.Logger;

public class ScheduledJobService extends JobService {

    private final ILog logger = Logger.withTag("MyLog");

    public ScheduledJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        logger.log("ScheduledJobService in onStartJob");
        ScheduledSanderService.startActionSendToDatabase(getApplicationContext());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        logger.log("ScheduledJobService in onStopJob");
        return false;
    }
}
