package com.practice.placetracker.ui.location;

import com.practice.placetracker.model.cache.SessionCache;
import com.practice.placetracker.ui.arch.Contract;

public interface LocationContract {

    interface View extends Contract.View {

        void updateTrackingInformation(SessionCache currentSession);

        void requestPermission();

        void setButtonsState(boolean isTracking);

        void setTime(String time);

        boolean isGrantedPermission();

        void startService();

        void stopService();
    }

    interface Presenter extends Contract.Presenter<View> {
        void startLocationTracking();

        void stopLocationTracking();

        void logOut();

        void stopTimer();

        void setupUiObservables();
    }

    interface Host extends Contract.Host {

        void showInitialFragment();
    }
}

