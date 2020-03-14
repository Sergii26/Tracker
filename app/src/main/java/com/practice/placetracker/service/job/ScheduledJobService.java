package com.practice.placetracker.service.job;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;

import com.practice.placetracker.App;
import com.practice.placetracker.model.dao.location.DatabaseWorker;
import com.practice.placetracker.model.dao.location.TrackedLocationSchema;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.model.network.location.LocationsNetwork;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
//      additional rx code only for invoke DB in not UI thread
        disposables.add(Observable.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        logger.log("ScheduledJobService in onStartJob");
                        logger.log("ScheduledSenderService in handleActionSendToDatabase");
                        final DatabaseWorker dbWorker = new DatabaseWorker(App.getInstance().getAppComponent().getLocationDao());
                        final List<TrackedLocationSchema> locations = dbWorker.getLocationsBySent(false);
                        logger.log("ScheduledSenderService in handleActionSendToDatabase DATABASE SIZE = " + locations.size());
                        if (locations.size() > 0) {
                            dbWorker.showSizeInLog();
                            final LocationsNetwork locationsNetwork = App.getInstance().getAppComponent().getLocationsNetwork();
                            for (TrackedLocationSchema location : locations) {
                                disposables.add(locationsNetwork.sendLocation(location)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(result -> {
                                                    if (result.isFail()) {
                                                        logger.log("ScheduledSenderService in handleActionSendToDatabase onFailure msg: " + result.getError().getMessage());
                                                        schedule(App.getInstance().getAppContext());
                                                    } else {
                                                        logger.log("ScheduledSenderService in handleActionSendToDatabase onSuccess");
                                                        dbWorker.updateLocation(true, location.getUniqueId());
                                                        dbWorker.deleteLocation(location);
                                                    }
                                                }
                                        ));
                            }
                        }
                    }
                }));

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        logger.log("ScheduledJobService in onStopJob");
        return false;
    }
}

