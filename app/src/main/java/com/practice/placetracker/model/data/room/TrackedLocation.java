package com.practice.placetracker.model.data.room;

import android.location.Location;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "locations")
public class TrackedLocation {

    @PrimaryKey(autoGenerate = true)
    private long uniqueId;
    private double latitude;
    private double longitude;
    private long time;
    private float accuracy;
    private boolean isSent;
    private String userEmail;


    public TrackedLocation(long uniqueId, double latitude, double longitude, long time, float accuracy, boolean isSent, String userEmail) {
        this.uniqueId = uniqueId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.accuracy = accuracy;
        this.isSent = isSent;
        this.userEmail = userEmail;
    }

    @Ignore
    public TrackedLocation(Location location, boolean isSent, String userEmail){
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.time = location.getTime();
        this.accuracy = location.getAccuracy();
        this.isSent = isSent;
        this.userEmail = userEmail;
    }

    public long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
