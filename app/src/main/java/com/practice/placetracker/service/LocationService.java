package com.practice.placetracker.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.practice.placetracker.R;
import com.practice.placetracker.service.job_service.CloudDatabaseIntentService;
import com.practice.placetracker.ui.MainActivity;
import com.practice.placetracker.ui.authorization_fragment.AuthorizationFragment;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class LocationService extends Service implements LocationServiceContract.BaseLocationService {
    private static final String CHANNEL_NAME = "Foreground Service Channel";

    @Inject
    LocationServiceContract.BaseLocationServicePresenter presenter;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i("MyLog", "LocationService - in onCreate()");
        DaggerServiceComponent.builder().serviceModule(new ServiceModule(this)).build().injectLocationService(this);
    }

    public void sendIntentToReceiver(){
        Intent intent = new Intent(CloudDatabaseIntentService.ACTION_SEND_TO_DATABASE);
        sendBroadcast(intent);
        Log.i("MyLog", "LocationService - in sendIntentToReceiver()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MyLog", "LocationService - in onStartCommand()");
        presenter.setUserEmail(intent.getStringExtra(AuthorizationFragment.KEY_EMAIL));
        presenter.startTracking();
        presenter.startForegroundService();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("MyLog", "LocationService - in onDestroy()");
        presenter.stopTracking();
        super.onDestroy();
    }

    public void createNotificationChannel(String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    channelId,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    public void startForegroundWithNotification(String channelId){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.gps_icon)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }

    public boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = false;
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }
        Log.i("MyLog", "LocationService - in isConnectedToNetwork() return - " + isConnected);
        return isConnected;
    }

}
