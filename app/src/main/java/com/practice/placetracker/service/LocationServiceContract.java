package com.practice.placetracker.service;

public interface LocationServiceContract {
    public interface BaseLocationService{
        void createNotificationChannel(String channelId);
        void startForegroundWithNotification(String channelId);
        boolean isConnectedToNetwork();
        void sendIntentToReceiver();
    }

    public interface BaseLocationServicePresenter{
        void startTracking();
        void stopTracking();
        void setUserEmail(String userEmail);
        void startForegroundService();
    }
}
