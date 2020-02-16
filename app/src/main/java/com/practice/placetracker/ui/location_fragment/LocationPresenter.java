package com.practice.placetracker.ui.location_fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.practice.placetracker.R;
import com.practice.placetracker.model.data.current_session.CurrentTrackingSession;
import com.practice.placetracker.model.data.room.LocationDatabase;
import com.practice.placetracker.model.data.room.TrackedLocation;

import java.util.List;
import java.util.Locale;

import androidx.core.content.ContextCompat;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LocationPresenter implements LocationContract.LocationBasePresenter {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private LocationContract.LocationView view;
    private CurrentTrackingSession currentSession;
    private LocationDatabase db;
    private FirebaseAuth mAuth;
    private boolean isTracking;
    private long startTime;
    private long millisFromStart;
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

    LocationPresenter(final LocationContract.LocationView view) {
        this.view = view;
        db = LocationDatabase.getInstance(view.getAppContext());
        currentSession = CurrentTrackingSession.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public void startObserveDatabase() {
        Log.i("MyLog", "LocationPresenter - startObserveDatabase() Start observing");
        Disposable disposable = db.locationDao().getAllLocationsRx()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TrackedLocation>>() {
                    @Override
                    public void accept(List<TrackedLocation> trackedLocations) throws Exception {
                        Log.i("MyLog", "LocationPresenter - startObserveDatabase() accept");
                        view.updateTrackingInformation(currentSession);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("MyLog", "LocationPresenter - startObserveDatabase() error: " + throwable.getMessage());
                    }
                });
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
        isTracking = false;
        view.setButtonsState(isTracking);
        startTime = 0;
        stopTimer();
    }

    public boolean isGrantedPermission() {
        return ContextCompat.checkSelfPermission(view.getViewActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void onPermissionDenied() {
        Log.i("MyLog", "LocationPresenter - onPermissionDenied()");
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
                    Log.i("MyLog", "Fragment - onRequestPermissionsResult() permission granted");
                    startLocationTracking();
                }

            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Log.i("MyLog", "Fragment - onRequestPermissionsResult() permission denied");
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
        handler.removeCallbacks(showTime);
    }

    public void logOut(){
        mAuth.signOut();
    }

    public void showForegroundFragment(){
        view.stopLocationService();
        currentSession.clearCurrentSession();
        view.openForegroundFragment();
    }

}
