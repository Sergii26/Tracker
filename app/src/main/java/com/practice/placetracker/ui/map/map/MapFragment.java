package com.practice.placetracker.ui.map.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.practice.placetracker.App;
import com.practice.placetracker.R;
import com.practice.placetracker.model.dao.TrackedLocationSchema;
import com.practice.placetracker.model.dao.map.MapDaoWorker;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.model.utils.AndroidUtil;
import com.practice.placetracker.ui.arch.fragments.MvpFragment;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MapFragment extends MvpFragment<MapContract.Presenter, MapContract.Host>
        implements MapContract.View, OnMapReadyCallback {

    private final ILog logger = Logger.withTag("MyLog");

    private MapView mapView;
    private GoogleMap googleMap;
    private Marker marker;

    @Inject
    MapContract.Presenter presenter;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            logger.log("MapFragment receiver");
            presenter.onNetworkConnectionChange();
        }
    };

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        logger.log("MapFragment in onCreate()");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        DaggerMapFragmentComponent.builder()
                .mapFragmentModule(new MapFragmentModule())
                .build().injectMapFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        logger.log("MapFragment in onCreateView()");
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        logger.log("MapFragment in onViewCreated()");
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        logger.log("MapFragment in onResume()");
        registerReceiver();
        presenter.showMap();
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        logger.log("MapFragment in onPause()");
        requireActivity().unregisterReceiver(receiver);
        presenter.deleteMarker();
        presenter.setLastLocation(null);
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        logger.log("MapFragment in onDestroy()");
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        logger.log("MapFragment in onLowMemory()");
        super.onLowMemory();
        mapView.onLowMemory();
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
            logger.log("MapFragment in onOptionsItemSelected()");
            presenter.logOut();
            if (hasCallBack()) {
                getCallBack().showInitialFragment();
            }
        }
        return true;
    }

    public void initMap() {
        logger.log("MapFragment initMap()");
        try {
            MapsInitializer.initialize(requireActivity().getApplicationContext());
        } catch (Exception e) {
            logger.log("MapFragment initMap ERROR = " + e.getMessage());
        }
        mapView.getMapAsync(this);
    }

    @Override
    protected MapContract.Presenter getPresenter() {
        logger.log("MapFragment in getPresenter()");
        return presenter;
    }

    public void addPolylines(List<TrackedLocationSchema> locations) {
        logger.log("MapFragment addPolylines() size: " + locations.size());
        presenter.deleteMarker();
        CustomCap cc = new CustomCap(BitmapDescriptorFactory.fromBitmap(getDrawableAsBitmap()), 12);
        if(presenter.getLastLocation() != null){
            logger.log("MapFragment addPolylines() lastLocation = " + presenter.getLastLocation().getLatitude());
            // for concat existing polylines with new locations
            locations.add(0, presenter.getLastLocation());
        }
        for (int i = 0; i < locations.size() - 1; i++) {
            PolylineOptions rectOptions = new PolylineOptions();
            rectOptions.add(new LatLng(locations.get(i).getLatitude(), locations.get(i).getLongitude()))
                    .add(new LatLng(locations.get(i + 1).getLatitude(), locations.get(i + 1).getLongitude()))
                    .startCap(cc)
                    .width(12);

            Polyline polyline = googleMap.addPolyline(rectOptions);
            polyline.setClickable(true);
        }
        TrackedLocationSchema lastLocation = locations.get(locations.size() - 1);
        presenter.setLastLocation(lastLocation);
        marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(getDrawableAsBitmap())));
        marker.setTitle(getString(R.string.marker_title));
    }

    public void removeMarker(){
        if(marker != null){
            marker.remove();
        }
    }

    private Bitmap getDrawableAsBitmap() {
        logger.log("MapFragment getDrawableAsBitmap()");
        Drawable drawable = getResources().getDrawable(R.drawable.polyline);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public void setCameraPosition(TrackedLocationSchema location, int zoom) {
        logger.log("MapFragment setCameraPosition()");
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(zoom).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        logger.log("MapFragment onMapReady()");
        this.googleMap = googleMap;
        presenter.showLocations();

    }

    @Override
    public boolean isConnectedToNetwork() {
        return AndroidUtil.isConnectedToNetwork(requireActivity());
    }

    public void registerReceiver(){
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        requireActivity().registerReceiver(receiver, filter);
    }

    @VisibleForTesting
    public MapView getMapView() {
        return mapView;
    }

}
