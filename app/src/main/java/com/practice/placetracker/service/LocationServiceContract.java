package com.practice.placetracker.service;

public interface LocationServiceContract {
    public interface Service {
        void createNotificationChannel(String channelId);
        void startForegroundWithNotification(String channelId);
        boolean isConnectedToNetwork();
        void sendIntentToReceiver();
    }

    public interface Presenter {
        void startTracking();
        void stopTracking();
        void setUserEmail(String userEmail);
        void startForegroundService();
        void disposeDisposable();
    }
}
