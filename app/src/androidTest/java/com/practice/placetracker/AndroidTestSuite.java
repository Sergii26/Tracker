package com.practice.placetracker;

import com.practice.placetracker.ui.auth.AuthFragmentLoginModeTest;
import com.practice.placetracker.ui.auth.AuthFragmentRegistrationModeTest;
import com.practice.placetracker.ui.initial.InitialFragmentTest;
import com.practice.placetracker.ui.map.MapFragmentTest;
import com.practice.placetracker.ui.tracker.LocationFragmentTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({InitialFragmentTest.class, AuthFragmentRegistrationModeTest.class, AuthFragmentLoginModeTest.class,
        LocationFragmentTest.class, MapFragmentTest.class})
public class AndroidTestSuite {
}
