package com.practice.placetracker.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.practice.placetracker.App;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;

public class ServiceInterruptReceiver extends BroadcastReceiver {

    private final ILog logger = Logger.withTag("MyLog");

    public static final String ACTION_INTERRUPT_SERVICE = "com.practice.placetracker.action.INTERRUPT_SERVICE";

    @Override
    public void onReceive(Context context, Intent intent) {
        logger.log("ServiceInterruptReceiver in onReceive");
        switch (intent.getAction()) {
            case ACTION_INTERRUPT_SERVICE:
                logger.log("ServiceInterruptReceiver in onReceive - action was found");
                context.stopService(new Intent(App.getInstance(), LocationService.class));
                break;
            default:
                throw new IllegalArgumentException("Unknown action.");
        }
    }

}

