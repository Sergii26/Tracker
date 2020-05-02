package com.practice.placetracker.ui.tracker.location;

import com.practice.placetracker.model.cache.SessionCache;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.network.auth.AuthNetwork;
import com.practice.placetracker.model.prefs.Prefs;
import com.practice.placetracker.ui.arch.MvpPresenter;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class LocationPresenter extends MvpPresenter<LocationContract.View> implements LocationContract.Presenter {

    private final ILog logger;
    private final AuthNetwork authNetwork;
    private final SessionCache sessionCache;
    private final Prefs prefs;

    private Disposable timer = Disposables.disposed();

    public LocationPresenter(SessionCache sessionCache, AuthNetwork authNetwork, ILog logger, Prefs prefs) {
        this.sessionCache = sessionCache;
        this.authNetwork = authNetwork;
        this.logger = logger;
        this.prefs = prefs;
    }

    @Override
    public void startLocationTracking() {
        logger.log("LocationPresenter in startLocationTracking()");
        if (hasView()) {
            if (view.isGrantedPermission()) {
                view.setButtonsState(true);
                sessionCache.setIsTracking(true);
                view.startService();
                startTimer();
            } else {
                view.requestPermission();
            }
        }
    }

    @Override
    public void stopLocationTracking() {
        logger.log("LocationPresenter in stopLocationTracking()");
        if(hasView()){
            view.setButtonsState(false);
            view.stopService();
        }
        sessionCache.setIsTracking(false);
        stopTimer();
    }

    private void startTimer() {
        logger.log("LocationPresenter in startTimer()");
        if (sessionCache.getStartTime() == 0) {
            sessionCache.putStartTime(System.currentTimeMillis());
        }
        if(!timer.isDisposed()){
            logger.log("LocationPresenter in startTimer() - disposing timer");
            timer.dispose();
        }
        timer = Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .filter(secondsFromStart -> LocationPresenter.this.hasView())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long secondsFromStart) throws Exception {
                        final long seconds = (System.currentTimeMillis() - sessionCache.getStartTime()) / 1000;
                        final long minutes = seconds / 60;
                        final long hours = minutes / 60;
                        final String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes % 60, seconds % 60);
                        view.setTime(time);
                    }
                });
    }

    public void stopTimer() {
        logger.log("LocationPresenter in stopTimer()");
        timer.dispose();
        sessionCache.putStartTime(0);
    }

    @Override
    public void setupUiObservables() {
        logger.log("LocationPresenter in setupUiObservables()");
        onStopDisposable.add(sessionCache.getTrackingStateObservable()
                .filter(isTracking -> hasView())
                .subscribe(isTracking -> {
                    view.setButtonsState(isTracking);
                    if (isTracking) {
                        logger.log("LocationPresenter in setupUiObservables() before startTimer()");
                        startTimer();
                    }
                }));

        onStopDisposable.add(sessionCache.getLocationsCountObservable()
                .filter(locations -> hasView())
                .subscribe(locations -> view.updateTrackingInformation(sessionCache)));
    }

    @Override
    public void logOut() {
        logger.log("LocationPresenter in logOut()");
        stopTimer();
        prefs.putEmail("");
        view.stopService();
        sessionCache.drop();
        authNetwork.logOut();

    }

    @Override
    public void unsubscribe() {
        logger.log("LocationPresenter in unsubscribe()");
        timer.dispose();
        super.unsubscribe();
    }

}

