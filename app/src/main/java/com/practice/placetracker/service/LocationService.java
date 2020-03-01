package com.practice.placetracker.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;

import com.practice.placetracker.R;
import com.practice.placetracker.android_utils.ILog;
import com.practice.placetracker.android_utils.Logger;
import com.practice.placetracker.service.job_service.ScheduledSanderService;
import com.practice.placetracker.ui.MainActivity;
import com.practice.placetracker.ui.authorization_fragment.FragmentIndication;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class LocationService extends android.app.Service implements LocationServiceContract.Service {

    private final ILog logger = Logger.withTag("MyLog");

    private static final String CHANNEL_NAME = "Foreground Service Channel";
    public static final String ACTION_SERVICE_IS_STOPPED = "com.practice.placetracker.action.SERVICE_IS_STOPPED";

    @Inject
    LocationServiceContract.Presenter presenter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        logger.log("LocationService in onCreate()");
        DaggerServiceComponent.builder().serviceModule(new ServiceModule(this)).build().injectLocationService(this);
    }

    public void sendIntentToReceiver(){
        Intent intent = new Intent(ScheduledSanderService.ACTION_SEND_TO_DATABASE);
        sendBroadcast(intent);
        logger.log("LocationService in sendIntentToReceiver()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        logger.log("LocationService in onStartCommand()");
        presenter.setUserEmail(intent.getStringExtra(FragmentIndication.KEY_EMAIL));
        presenter.startTracking();
        presenter.startForegroundService();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        logger.log("LocationService in onDestroy()");
        presenter.stopTracking();
        presenter.disposeDisposable();
        sendIntentToFragment();
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

        Intent interrupIntent = new Intent(this, ServiceInterruptReceiver.class);
        interrupIntent.setAction(ServiceInterruptReceiver.ACTION_INTERRUPT_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
//        }
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(this, 0, interrupIntent, 0);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.gps_icon)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.gps_icon, getString(R.string.btn_stop),
                        snoozePendingIntent)
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
        logger.log("LocationService in isConnectedToNetwork() return - " + isConnected);
        return isConnected;
    }

    public void sendIntentToFragment(){
        logger.log("LocationService in sendIntentToFragment()");
        Intent intent = new Intent(ACTION_SERVICE_IS_STOPPED);
        sendBroadcast(intent);
    }

}
