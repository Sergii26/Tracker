package com.practice.placetracker.model.data.current_session;


public class CurrentTrackingSession {

    public static final int TIME_INDICATION = 0;
    public static final int DISTANCE_INDICATION = 1;

    private static CurrentTrackingSession session;
    private int locationCount;
    private int locationsByTime;
    private int locationsByDistance;

    public static CurrentTrackingSession getInstance(){
        if(session == null){
            return session = new CurrentTrackingSession();
        }
        return session;
    }

    public void updateSession(int indication){
        locationCount++;

        if (indication == TIME_INDICATION) {
            locationsByTime++;
        }
        if(indication == DISTANCE_INDICATION){
            locationsByDistance++;
        }
    }

    public void clearCurrentSession(){
        locationCount = 0;
        locationsByTime = 0;
        locationsByDistance = 0;
    }

    public int getLocationCount() {
        return locationCount;
    }

    public int getLocationsByTime() {
        return locationsByTime;
    }

    public int getLocationsByDistance() {
        return locationsByDistance;
    }

}
