package com.practice.placetracker.rule;

import android.app.Activity;

import com.practice.placetracker.App;
import com.practice.placetracker.ui.tracker.MainActivity;

import androidx.test.rule.ActivityTestRule;

public class OpenLoginFragmentTestRule<T extends Activity> extends ActivityTestRule {

    public OpenLoginFragmentTestRule(Class activityClass) {
        super(activityClass);
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();
        App.getInstance().getAppComponent().getPrefs().putEmail("test@test.ua");
        MainActivity activity = (MainActivity) getActivity();
        activity.showLoginFragment();

    }
}
