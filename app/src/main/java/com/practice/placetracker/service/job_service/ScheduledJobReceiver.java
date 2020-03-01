package com.practice.placetracker.service.job_service;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.practice.placetracker.android_utils.ILog;
import com.practice.placetracker.android_utils.Logger;

public class ScheduledJobReceiver extends BroadcastReceiver {

    private final ILog logger = Logger.withTag("MyLog");

    private static final String ACTION_SEND_TO_DATABASE = "com.practice.placetracker.action.SEND_TO_DATABASE";

    private static int sJobId = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        logger.log("ScheduledJobReceiver in onReceive");
        switch (intent.getAction()) {
            case ACTION_SEND_TO_DATABASE:
                logger.log("ScheduledJobReceiver in onReceive - action was found");
                scheduleJob(context);
                break;
            default:
                throw new IllegalArgumentException("Unknown action.");
        }
    }

    private void scheduleJob(Context context) {
        ComponentName jobService = new ComponentName(context, ScheduledJobService.class);
        JobInfo.Builder exerciseJobBuilder = new JobInfo.Builder(sJobId++, jobService);
        exerciseJobBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        exerciseJobBuilder.setRequiresDeviceIdle(false);
        exerciseJobBuilder.setRequiresCharging(false);

        logger.log("ScheduledJobReceiver in scheduleJob - adding job to scheduler");

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.schedule(exerciseJobBuilder.build());
        }
    }
}
