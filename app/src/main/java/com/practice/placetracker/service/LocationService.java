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
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.service.job.ScheduledJobService;
import com.practice.placetracker.ui.tracker.MainActivity;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class LocationService extends android.app.Service implements LocationServiceContract.Service {

    private final ILog logger = Logger.withTag("MyLog");

    private static final String CHANNEL_NAME = "Foreground Service Channel";
    public static final String ACTION_SERVICE_IS_STOPPED = "com.practice.placetracker.action.SERVICE_IS_STOPPED";
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        logger.log("LocationService in onStartCommand()");
        createNotificationChannel(CHANNEL_ID);
        startForegroundWithNotification(CHANNEL_ID);
        presenter.startTracking();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        logger.log("LocationService in onDestroy()");
        presenter.stopTracking();
        sendIntentToFragment();
        super.onDestroy();
    }

    @Override
    public void scheduleJob() {
        ScheduledJobService.schedule(this);
    }

    private void createNotificationChannel(String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel serviceChannel = new NotificationChannel(
                    channelId,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            final NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    private void startForegroundWithNotification(String channelId) {
        final Intent notificationIntent = new Intent(this, MainActivity.class);

        final Intent interruptIntent = new Intent(this, ServiceInterruptReceiver.class);
        interruptIntent.setAction(ServiceInterruptReceiver.ACTION_INTERRUPT_SERVICE);
        final PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(this, 0, interruptIntent, 0);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        final Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.gps_icon)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.gps_icon, getString(R.string.btn_stop),
                        snoozePendingIntent)
                .build();
        startForeground(1, notification);
    }

    @Override
    public boolean isConnectedToNetwork() {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = false;
        if (connectivityManager != null) {
            final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }
        logger.log("LocationService in isConnectedToNetwork() return - " + isConnected);
        return isConnected;
    }

    public void sendIntentToFragment() {
        logger.log("LocationService in sendIntentToFragment()");
        final Intent intent = new Intent(ServiceInterruptReceiver.ACTION_INTERRUPT_SERVICE);
        sendBroadcast(intent);
    }
}

