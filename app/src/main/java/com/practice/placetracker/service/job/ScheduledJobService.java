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
import com.practice.placetracker.model.network.location.LocationsNetwork;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
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
        final AppComponent appComponent = App.getInstance().getAppComponent();
        final LocationsNetwork locationsNetwork = appComponent.getLocationsNetwork();
        disposables.add(Observable.just(appComponent.provideDatabaseWorker())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(worker -> logger.log("ScheduledSenderService in handleActionSendToDatabase"))
                .flatMap(worker -> {
                    return Observable.just(worker.getLocationsBySent(false))
                            .doOnNext(locations -> logger.log("ScheduledSenderService in handleActionSendToDatabase DATABASE SIZE = " + locations.size()))
                            .flatMap(locations -> Observable.fromIterable(locations))
                            .flatMap(location -> locationsNetwork.sendLocation(location)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnError(error -> {
                                        logger.log("ScheduledSenderService in handleActionSendToDatabase onFailure msg: " + error.getMessage());
                                        schedule(App.getInstance().getAppContext());
                                    })
                                    .flatMap(result -> {
                                        if (result.isFail()) {
                                            logger.log("ScheduledSenderService in handleActionSendToDatabase onFailure msg: " + result.getError().getMessage());
                                            schedule(App.getInstance().getAppContext());
                                            return Single.never();
                                        } else {
                                            logger.log("ScheduledSenderService in handleActionSendToDatabase onSuccess");
                                            return worker.deleteLocation(location)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .doOnComplete(() -> logger.log("ScheduledSenderService delete location - success"))
                                                    .doOnError(throwable -> logger.log("ScheduledSenderService delete location - failure" + throwable.getMessage()))
                                                    .toSingleDefault(new Object()); // completable - to single
                                        }
                                    }).toObservable());
                })
                .subscribe());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        logger.log("ScheduledJobService in onStopJob");
        return false;
    }

}

