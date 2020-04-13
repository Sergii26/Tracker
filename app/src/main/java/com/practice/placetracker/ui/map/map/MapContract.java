package com.practice.placetracker.ui.map.map;

import android.graphics.Bitmap;

import com.practice.placetracker.model.dao.TrackedLocationSchema;
import com.practice.placetracker.ui.arch.Contract;

import java.util.List;

public interface MapContract {


    interface View extends Contract.View {

        void initMap();

        void addPolylines(List<TrackedLocationSchema> locations);

        void setCameraPosition(TrackedLocationSchema location, int zoom);

        boolean isConnectedToNetwork();

        void removeMarker();
    }

    interface Presenter extends Contract.Presenter<View> {
        void logOut();

        void showLocations();

        void showMap();

        void onNetworkConnectionChange();

        void setLastLocation(TrackedLocationSchema location);

        TrackedLocationSchema getLastLocation();

        void deleteMarker();
    }

    interface Host extends Contract.Host {
        void showInitialFragment();
    }

}
