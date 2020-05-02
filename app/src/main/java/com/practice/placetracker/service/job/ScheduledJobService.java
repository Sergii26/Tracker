package com.practice.placetracker.service.job;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;

import com.practice.placetracker.App;
import com.practice.placetracker.AppComponent;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.model.network.Result;
import com.practice.placetracker.model.use_case.SavedLocationsSender;
import com.practice.placetracker.model.use_case.SendSavedLocationsUseCase;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ScheduledJobService extends JobService {

    private final ILog logger = Logger.withTag("MyLog");
    private static int sJobId = 1;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public static void schedule(Context context) {
        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.cancel(sJobId);
        }
        final ComponentName jobService = new ComponentName(context, ScheduledJobService.class);
        final JobInfo.Builder exerciseJobBuilder = new JobInfo.Builder(sJobId, jobService);
        exerciseJobBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        exerciseJobBuilder.setRequiresDeviceIdle(false);
        exerciseJobBuilder.setRequiresCharging(false);

        Logger.withTag("MyLog").log("ScheduledJobService in schedule - adding job to scheduler");
        if (jobScheduler != null) {
            jobScheduler.schedule(exerciseJobBuilder.build());
        }
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        logger.log("ScheduledJobService in onStartJob");
        final SavedLocationsSender sender = App.getInstance().getAppComponent().provideSendSaveLocationsUseCase();
        disposables.add(sender.sendSavedLocations()
                .subscribeOn(Schedulers.io())
                .subscribe(booleanResult -> {
                    logger.log("ScheduledJobService in onStartJob result: " + booleanResult.getData());
                    if(!booleanResult.getData()){
                        schedule(App.getInstance().getAppContext());
                    }
                }, throwable -> logger.log("ScheduledJobService in onStartJob error: " + throwable.getMessage())));
        logger.log("ScheduledJobService in onStartJob END METHOD ");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        logger.log("ScheduledJobService in onStopJob");
        return false;
    }

}

