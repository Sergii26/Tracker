package com.practice.placetracker.ui.location_fragment;

import android.app.Activity;
import android.content.Context;

import com.practice.placetracker.model.data.current_session.CurrentTrackingSession;

public interface LocationContract {
    interface View {
        void startLocationService(String collectionName);

        void stopLocationService();

        void updateTrackingInformation(CurrentTrackingSession currentSession);

        String getUserEmail();

        Activity getViewActivity();

        void makeToast(String message);

        String getStringFromResources(int stringId);

        void requestPermission();

        void setButtonsState(boolean isTracking);

        void setTime(String time);

        void openForegroundFragment();
    }

    interface Presenter {
        void startLocationTracking();

        void stopLocationTracking();

        void startObserveDatabase();

        void onPermissionDenied();

        void onRequestPermission(int requestCode, int[] grantResults);

        void restoreUI();

        void updateMillisFromStart();

        void showInitialFragment();

        void logOut();

        void stopObserveDatabase();

        void stopTimer();

        void setTrackingState(boolean isTracking);
    }
}
