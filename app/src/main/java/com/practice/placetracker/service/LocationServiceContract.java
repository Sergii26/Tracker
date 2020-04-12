package com.practice.placetracker.service;

public interface LocationServiceContract {
    interface Service {
        boolean isConnectedToNetwork();

        void scheduleJob();
    }

    interface Presenter {
        void startTracking();

        void stopTracking();
    }
}

