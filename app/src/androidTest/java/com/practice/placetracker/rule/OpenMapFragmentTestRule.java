package com.practice.placetracker.rule;

import android.app.Activity;

import com.google.android.gms.maps.MapView;
import com.practice.placetracker.App;
import com.practice.placetracker.AppComponent;
import com.practice.placetracker.DaggerAppComponent;
import com.practice.placetracker.DaggerAppTestComponent;
import com.practice.placetracker.R;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.ui.arch.HostActivity;
import com.practice.placetracker.ui.map.map.MapFragment;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.test.rule.ActivityTestRule;

public class OpenMapFragmentTestRule<T extends Activity> extends ActivityTestRule {

    private HostActivity activity;

    public OpenMapFragmentTestRule(Class activityClass) {
        super(activityClass);
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();
        App.getInstance().getAppComponent().getPrefs().putEmail(getActivity().getString(R.string.email_for_test));
        activity = (HostActivity) getActivity();
        activity.addFragment(MapFragment.newInstance(), false, "map");

    }

    @Override
    protected void beforeActivityLaunched() {
        super.beforeActivityLaunched();
        App.getInstance().setComponents(DaggerAppTestComponent.create());
    }

    public MapView getMapView(){
        List<Fragment> fragments = activity.getSupportFragmentManager().getFragments();
        MapFragment mapFragment = (MapFragment)fragments.get(fragments.size()-1);
        return mapFragment.getMapView();
    }
}
