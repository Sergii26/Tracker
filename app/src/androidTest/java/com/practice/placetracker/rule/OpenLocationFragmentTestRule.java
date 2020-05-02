package com.practice.placetracker.rule;

import android.app.Activity;

import com.practice.placetracker.App;
import com.practice.placetracker.R;
import com.practice.placetracker.ui.tracker.MainActivity;

import androidx.test.rule.ActivityTestRule;

public class OpenLocationFragmentTestRule<T extends Activity> extends ActivityTestRule {

    public OpenLocationFragmentTestRule(Class activityClass) {
        super(activityClass);
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();
        App.getInstance().getAppComponent().getPrefs().putEmail(getActivity().getString(R.string.email_for_test));
        MainActivity activity = (MainActivity) getActivity();
        activity.showLocationFragment();
    }

}
