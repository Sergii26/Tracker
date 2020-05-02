package com.practice.placetracker.ui.map;

import android.graphics.Rect;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.practice.placetracker.rule.OpenMapFragmentTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.UiThreadTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import static org.junit.Assert.assertEquals;

public class MapFragmentTest {

    private GoogleMap.OnPolylineClickListener polylineListener;
    private GoogleMap.OnMarkerClickListener markerListener;

    @Rule
    public OpenMapFragmentTestRule<MainActivity> mActivityRule =
            new OpenMapFragmentTestRule<>(MainActivity.class);

    @Rule
    public UiThreadTestRule threadTestRule = new UiThreadTestRule();

    @Before
    public void beforeEach(){
        final OnMapReadyCallback onMapReadyCallback = googleMap -> {
            googleMap.setOnPolylineClickListener(polylineListener);
            googleMap.setOnMarkerClickListener(markerListener);
        };

        try {
            threadTestRule.runOnUiThread(() -> mActivityRule.getMapView().getMapAsync(onMapReadyCallback));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Test
    public void MarkerAndLastPolylinePositionTest(){

        markerListener = marker -> {
            assertEquals(new LatLng(50.4556213, 30.3962601), marker.getPosition());
            return false;
        };

        polylineListener = polyline -> {
            List<LatLng> polylinePoints = polyline.getPoints();
            List<LatLng> expectedPoints = new ArrayList<>();
            expectedPoints.add(new LatLng(50.4550534, 30.396309));
            expectedPoints.add(new LatLng(50.4556213, 30.3962601));

            assertEquals( expectedPoints, polylinePoints);
        };

        //wait for map
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        //find marker
        UiObject mMarker1 = device.findObject(new UiSelector().descriptionContains("Last Location"));

        int markerPositionX = 0;
        int markerPostionY = 0;
        try {
            Rect i = mMarker1.getVisibleBounds();
            markerPositionX = i.centerX();
            markerPostionY = i.centerY();
            //click on marker
            mMarker1.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        //polyline is under marker so click on polyline
        device.click(markerPositionX, markerPostionY + 100);
    }

}
