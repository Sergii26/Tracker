package com.practice.placetracker.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.practice.placetracker.R;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.ui.arch.HostActivity;
import com.practice.placetracker.ui.auth.AuthContract;
import com.practice.placetracker.ui.auth.AuthFragment;
import com.practice.placetracker.ui.auth.FragmentIndication;
import com.practice.placetracker.ui.initial.InitialContract;
import com.practice.placetracker.ui.initial.InitialFragment;
import com.practice.placetracker.ui.location.LocationContract;
import com.practice.placetracker.ui.location.LocationFragment;
import com.practice.placetracker.ui.progress.ProgressDialogWrapper;

import androidx.fragment.app.FragmentManager;

public class MainActivity extends HostActivity implements InitialContract.Host,
        AuthContract.Host, LocationContract.Host {

    private final ILog logger = Logger.withTag("MyLog");
    private ProgressDialogWrapper progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportFragmentManager().getFragments().size() == 0) {
            logger.log("MainActivity in onCreate() add InitalFragment");
            addFragment(InitialFragment.newInstance());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected int getFragmentContainerId() {
        logger.log("MainActivity in getFragmentContainerId()");
        return R.id.fragment_container;
    }

    @Override
    public void showLoginFragment() {
        logger.log("MainActivity in showLoginFragment()");
        replaceFragmentToBackStack(AuthFragment.newInstance(FragmentIndication.LOGIN_MODE));
    }

    @Override
    public void showRegistrationFragment() {
        logger.log("MainActivity in showRegistrationFragment()");
        replaceFragmentToBackStack(AuthFragment.newInstance(FragmentIndication.REGISTRATION_MODE));
    }

    @Override
    public void showLocationFragment() {
        logger.log("MainActivity in showLocationFragment()");
        replaceFragment(LocationFragment.newInstance());
    }

    @Override
    public void showInitialFragment() {
        logger.log("MainActivity in showRegistrationFragment()");
        clearBackStack();
        replaceFragment(InitialFragment.newInstance());
    }

    private void clearBackStack() {
        logger.log("MainActivity in clearBackStack()");
        final FragmentManager manager = getSupportFragmentManager();
        while (manager.getBackStackEntryCount() > 0) {
            manager.popBackStackImmediate();
        }
    }

    @Override
    public boolean hasProgress() {
        logger.log("MainActivity in hasProgress()");
        return true;
    }

    @Override
    public void showProgress(String title, String message) {
        logger.log("MainActivity in showProgress()");
        if (progress == null) {
            progress = new ProgressDialogWrapper(this, title, message);
        }
        if (!progress.isShowing()) {
            progress.show();
        }
    }

    @Override
    public void showProgress(int titleRes, int messageRes) {
        logger.log("MainActivity in showProgress()");
        if (progress == null) {
            progress = new ProgressDialogWrapper(this, titleRes, messageRes);
        }
        if (!progress.isShowing()) {
            progress.show();
        }
    }

    @Override
    public void showProgress() {
        logger.log("MainActivity in showProgress()");
        if (progress == null) {
            progress = new ProgressDialogWrapper(this);
        }
        if (!progress.isShowing()) {
            progress.show();
        }
    }

    @Override
    public void hideProgress() {
        logger.log("MainActivity in hideProgress()");
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }

}

