package com.practice.placetracker.ui.location_fragment;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.practice.placetracker.R;
import com.practice.placetracker.android_utils.ILog;
import com.practice.placetracker.android_utils.Logger;
import com.practice.placetracker.model.data.current_session.CurrentTrackingSession;
import com.practice.placetracker.service.LocationService;
import com.practice.placetracker.ui.FragmentChanger;
import com.practice.placetracker.ui.initial_fragment.InitialFragment;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.practice.placetracker.service.LocationService.ACTION_SERVICE_IS_STOPPED;
import static com.practice.placetracker.ui.authorization_fragment.FragmentIndication.KEY_EMAIL;

public class LocationFragment extends androidx.fragment.app.Fragment implements LocationContract.View, android.view.View.OnClickListener {

    private final ILog logger = Logger.withTag("MyLog");

    private Unbinder unbinder;

    @BindView(R.id.btnStart)
    Button btnStart;
    @BindView(R.id.btnStop)
    Button btnStop;
    @BindView(R.id.tvLocationsCount)
    TextView tvLocationsCount;
    @BindView(R.id.tvLocationsByTime)
    TextView tvLocationsByTime;
    @BindView(R.id.tvLocationsByDistance)
    TextView tvLocationsByDistance;
    @BindView(R.id.tvTime)
    TextView tvTime;

    @Inject
    LocationContract.Presenter presenter;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            logger.log("LocationFragment in onReceive()");
            presenter.stopLocationTracking();
        }
    };

    public LocationFragment() {
    }

    public static LocationFragment newInstance(String userEmail) {
        LocationFragment locationFragment = new LocationFragment();
        final Bundle bundle = new Bundle();
        bundle.putString(KEY_EMAIL, userEmail);
        locationFragment.setArguments(bundle);
        return locationFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        DaggerLocationFragmentComponent.builder().locationFragmentModule(new LocationFragmentModule(this)).build().injectLocationFragment(this);
        registerReceiver();
    }

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        logger.log("LocationFragment in onPause()");
        presenter.stopObserveDatabase();
    }

    @Override
    public void onResume() {
        super.onResume();
        logger.log("LocationFragment in onResume()");
        presenter.startObserveDatabase();
        presenter.restoreUI();
        presenter.updateMillisFromStart();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        logger.log("LocationFragment in onDestroyView()");
        getActivity().unregisterReceiver(receiver);
        unbinder.unbind();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.action_log_out);
        menuItem.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_log_out) {
            logger.log("LocationFragment in onOptionsItemSelected()");
            presenter.logOut();
            presenter.showInitialFragment();
        }
        return true;
    }

    @Override
    public void onClick(android.view.View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                presenter.startLocationTracking();
                break;
            case R.id.btnStop:
                presenter.stopLocationTracking();
                break;
        }
    }

    public void startLocationService(String userEmail) {
        logger.log("LocationFragment in startLocationService()");
        Intent intent = new Intent(getActivity(), LocationService.class);
        intent.putExtra(KEY_EMAIL, userEmail);
        getActivity().startService(intent);
    }

    public void stopLocationService() {
        Objects.requireNonNull(getActivity()).stopService(new Intent(getActivity(), LocationService.class));
    }

    public void updateTrackingInformation(CurrentTrackingSession currentSession) {
        tvLocationsCount.setText(String.valueOf(currentSession.getLocationCount()));
        tvLocationsByDistance.setText(String.valueOf(currentSession.getLocationsByDistance()));
        tvLocationsByTime.setText(String.valueOf(currentSession.getLocationsByTime()));
    }

    public String getUserEmail() {
        String userEmail = this.getArguments().getString(KEY_EMAIL);
        logger.log("LocationFragment in getStringFromIntent() ExtraString = " + userEmail);
        return userEmail;
    }

    public Activity getViewActivity() {
        return getActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        logger.log("LocationFragment in onRequestPermissionsResult()");
        presenter.onRequestPermission(requestCode, grantResults);
    }

    @Override
    public void makeToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    public String getStringFromResources(int stringId) {
        logger.log("LocationFragment in getStringFromResources()");
        return getString(stringId);
    }

    public void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LocationPresenter.MY_PERMISSIONS_REQUEST_LOCATION);
    }

    public void setButtonsState(boolean isTracking) {
        if (isTracking) {
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);
        } else {
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
        }
    }

    public void setTime(String time) {
        tvTime.setText(time);
    }

    public void openForegroundFragment() {
        logger.log("LocationFragment in openForegroundFragment()");
        Fragment fragment = InitialFragment.newInstance();
        ((FragmentChanger) getActivity()).openFragment(fragment, true);
    }

    public void registerReceiver(){
        IntentFilter filter = new IntentFilter(ACTION_SERVICE_IS_STOPPED);
        getActivity().registerReceiver(receiver, filter);
    }
}
