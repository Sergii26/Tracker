package com.practice.placetracker.ui.location;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.practice.placetracker.R;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.model.cache.SessionCache;
import com.practice.placetracker.service.LocationService;
import com.practice.placetracker.ui.arch.fragments.MvpFragment;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.practice.placetracker.service.LocationService.ACTION_SERVICE_IS_STOPPED;
import static com.practice.placetracker.service.ServiceInterruptReceiver.ACTION_INTERRUPT_SERVICE;

public class LocationFragment extends MvpFragment<LocationContract.Presenter, LocationContract.Host> implements LocationContract.View, android.view.View.OnClickListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
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
            // inform presenter that tracking was stopped from notification
            logger.log("LocationFragment in onReceive()");
            presenter.stopLocationTracking();
        }
    };


    public static LocationFragment newInstance() {
        return new LocationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        DaggerLocationFragmentComponent.builder()
                .locationFragmentModule(new LocationFragmentModule())
                .build().injectLocationFragment(this);
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
    public void onStart() {
        super.onStart();
        logger.log("LocationFragment in onStart()");
        getPresenter().setupUiObservables();
    }

    @Override
    public void onDestroyView() {
        logger.log("LocationFragment in onDestroyView()");
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    protected LocationContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        final MenuItem menuItem = menu.findItem(R.id.action_log_out);
        menuItem.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_log_out) {
            logger.log("LocationFragment in onOptionsItemSelected()");
            presenter.logOut();
            if (hasCallBack()) {
                getCallBack().showInitialFragment();
            }
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

    public void updateTrackingInformation(SessionCache currentSession) {
        tvLocationsCount.setText(String.valueOf(currentSession.getLocationCount()));
        tvLocationsByDistance.setText(String.valueOf(currentSession.getLocationsByDistance()));
        tvLocationsByTime.setText(String.valueOf(currentSession.getLocationsByTime()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        logger.log("LocationFragment in onRequestPermissionsResult()");
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // location-related task you need to do.
                logger.log("LocationPresenter in onRequestPermissionsResult() permission granted");
                presenter.startLocationTracking();
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                logger.log("LocationPresenter in onRequestPermissionsResult() permission denied");
                showToast(getString(R.string.permission_denied));
            }
        }
    }

    public void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
    }

    public void setButtonsState(boolean isTracking) {
        btnStart.setEnabled(!isTracking);
        btnStop.setEnabled(isTracking);
    }

    public void setTime(String time) {
        tvTime.setText(time);
    }

    @Override
    public boolean isGrantedPermission() {
        return getActivity() != null && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void startService() {
        Objects.requireNonNull(getActivity()).startService(new Intent(getActivity(), LocationService.class));
    }

    @Override
    public void stopService() {
        Objects.requireNonNull(getActivity()).stopService(new Intent(getActivity(), LocationService.class));
    }

    public void registerReceiver(){
        IntentFilter filter = new IntentFilter(ACTION_INTERRUPT_SERVICE);
        Objects.requireNonNull(getActivity()).registerReceiver(receiver, filter);
    }

}
