package com.practice.placetracker.ui.location_fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.practice.placetracker.R;
import com.practice.placetracker.model.data.current_session.CurrentTrackingSession;
import com.practice.placetracker.service.LocationService;
import com.practice.placetracker.ui.authorization_fragment.AuthorizationFragment;
import com.practice.placetracker.ui.foreground_fragment.ForegroundFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LocationFragment extends Fragment implements LocationContract.LocationView, View.OnClickListener {

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

    private LocationContract.LocationBasePresenter presenter;
    private String userEmail;

    public LocationFragment() {
    }

    public static LocationFragment newInstance() {
        return new LocationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        presenter = new LocationPresenter(this);
        userEmail = this.getArguments().getString(AuthorizationFragment.KEY_EMAIL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.restoreUI();
        presenter.updateMillisFromStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
            Log.i("MyLog", "LocationFragment - onOptionsItemSelected()");
            presenter.logOut();
            presenter.showForegroundFragment();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                presenter.startLocationTracking();
                presenter.startObserveDatabase();
                break;
            case R.id.btnStop:
                presenter.stopLocationTracking();
                break;
        }
    }

    public void startLocationService(String collectionName) {
        Log.i("MyLog", "Fragment - startLocationService() inBundle email = " + userEmail);
        Intent intent = new Intent(getActivity(), LocationService.class);
        intent.putExtra(AuthorizationFragment.KEY_EMAIL, collectionName);
        getActivity().startService(intent);
    }

    public void stopLocationService() {
        getActivity().stopService(new Intent(getActivity(), LocationService.class));
    }

    public void updateTrackingInformation(CurrentTrackingSession currentSession) {
        tvLocationsCount.setText(String.valueOf(currentSession.getLocationCount()));
        tvLocationsByDistance.setText(String.valueOf(currentSession.getLocationsByDistance()));
        tvLocationsByTime.setText(String.valueOf(currentSession.getLocationsByTime()));
    }

    public String getUserEmail() {
        String email = this.getArguments().getString(AuthorizationFragment.KEY_EMAIL);
        Log.i("MyLog", "Fragment - getStringFromIntent() ExtraString = " + email);
        return email;
    }

    @Override
    public Context getAppContext() {
        return this.getContext();
    }

    public Activity getViewActivity() {
        return getActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("MyLog", "Fragment - onRequestPermissionsResult()");
        presenter.onRequestPermission(requestCode, grantResults);
    }

    @Override
    public void makeToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    public String getStringFromResources(int stringId) {
        Log.d("MyLog", "In LocationFragment at getStringFromResources()");
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
        Log.d("MyLog", "LocationFragment on openForegroundFragment()");
        ForegroundFragment fragment = ForegroundFragment.newInstance();
        FragmentTransaction transaction = null;
        if (getFragmentManager() != null) {
            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
        } else {
            Log.d("MyLog", "AuthorizationFragment on openLocationFragment() getFragmentManager() = null");
        }
    }

}
