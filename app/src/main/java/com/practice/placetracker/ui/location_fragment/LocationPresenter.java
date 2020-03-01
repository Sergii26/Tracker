package com.practice.placetracker.ui.location_fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.practice.placetracker.R;
import com.practice.placetracker.android_utils.ILog;
import com.practice.placetracker.android_utils.Logger;
import com.practice.placetracker.model.data.current_session.CurrentTrackingSession;
import com.practice.placetracker.model.data.room.IDatabase;
import com.practice.placetracker.model.data.room.TrackedLocation;

import java.util.List;
import java.util.Locale;

import androidx.core.content.ContextCompat;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class LocationPresenter implements LocationContract.Presenter {

    private final ILog logger = Logger.withTag("MyLog");

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private LocationContract.View view;
    private CurrentTrackingSession currentSession;
    private IDatabase db;
    private boolean isTracking;
    private long startTime;
    private long millisFromStart;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final Handler handler = new Handler();
    private Runnable showTime = new Runnable() {
        @Override
        public void run() {
            long hours = millisFromStart / 3600;
            long minutes = (millisFromStart % 3600) / 60;
            long secs = millisFromStart % 60;
            String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
            view.setTime(time);
            millisFromStart++;
            handler.postDelayed(this, 1000);
        }
    };

    LocationPresenter(final LocationContract.View view, IDatabase database, CurrentTrackingSession currentSession) {
        this.view = view;
        db = database;
        this.currentSession = currentSession;
    }

    public void setTrackingState(boolean isTracking){
        this.isTracking = isTracking;
    }

    public void startObserveDatabase() {
        logger.log("LocationPresenter in startObserveDatabase() Start observing");
        compositeDisposable.add(db.locationDao().getAllLocationsRx()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TrackedLocation>>() {
                    @Override
                    public void accept(List<TrackedLocation> trackedLocations) throws Exception {
                        logger.log("LocationPresenter in startObserveDatabase() accept");
                        view.updateTrackingInformation(currentSession);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        logger.log("LocationPresenter in startObserveDatabase() error: " + throwable.getMessage());
                    }
                }));
    }

    @Override
    public void startLocationTracking() {
        if (isGrantedPermission()) {
            view.startLocationService(view.getUserEmail());
            startObserveDatabase();
            isTracking = true;
            view.setButtonsState(isTracking);
            startTime = System.currentTimeMillis();
            millisFromStart = 0;
            startTimer();
        } else {
            view.requestPermission();
        }
    }


    @Override
    public void stopLocationTracking() {
        view.stopLocationService();
        stopObserveDatabase();
        isTracking = false;
        view.setButtonsState(isTracking);

        stopTimer();
    }

    public boolean isGrantedPermission() {
        return ContextCompat.checkSelfPermission(view.getViewActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void onPermissionDenied() {
        logger.log("LocationPresenter in onPermissionDenied()");
        view.makeToast(view.getStringFromResources(R.string.permission_denied));
    }

    public void onRequestPermission(int requestCode, int[] grantResults) {
        if (requestCode == LocationPresenter.MY_PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // location-related task you need to do.
                if (ContextCompat.checkSelfPermission(view.getViewActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    logger.log("LocationPresenter in onRequestPermissionsResult() permission granted");
                    startLocationTracking();
                }

            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                logger.log("LocationPresenter in onRequestPermissionsResult() permission denied");
                onPermissionDenied();
            }
        }
    }

    public void restoreUI() {
        view.updateTrackingInformation(currentSession);
        view.setButtonsState(isTracking);
    }

    public void updateMillisFromStart(){
        millisFromStart = (System.currentTimeMillis() - startTime) / 1000;
    }



    public void startTimer(){
        handler.post(showTime);
    }

    public void stopTimer() {
        startTime = 0;
        handler.removeCallbacks(showTime);
    }

    public void logOut(){
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void stopObserveDatabase() {
        logger.log("LocationPresenter in stopObserveDatabase() CompositeDisposable = " + compositeDisposable.size());
        if(compositeDisposable.size() != 0) {
            compositeDisposable.clear();
        }
        logger.log("LocationPresenter in stopObserveDatabase() CompositeDisposable = " + compositeDisposable.size());
    }

    public void showInitialFragment(){
        stopObserveDatabase();
        view.stopLocationService();
        currentSession.clearCurrentSession();
        view.openForegroundFragment();
    }

}
