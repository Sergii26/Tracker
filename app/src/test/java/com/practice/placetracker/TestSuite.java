package com.practice.placetracker;

import com.practice.placetracker.model.cache.SessionCacheImplTest;
import com.practice.placetracker.model.dao.MapDatabaseWorkerTest;
import com.practice.placetracker.model.use_case.SendSavedLocationsUseCaseTest;
import com.practice.placetracker.service.LocationServicePresenterTest;
import com.practice.placetracker.ui.auth.AuthPresenterLoginModeTest;
import com.practice.placetracker.ui.auth.AuthPresenterRegistrationModeTest;
import com.practice.placetracker.ui.initial.InitialPresenterTest;
import com.practice.placetracker.ui.map.MapPresenterTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({SessionCacheImplTest.class, MapDatabaseWorkerTest.class, LocationServicePresenterTest.class, AuthPresenterRegistrationModeTest.class,
        AuthPresenterLoginModeTest.class, InitialPresenterTest.class, MapPresenterTest.class, LocationServicePresenterTest.class, SendSavedLocationsUseCaseTest.class})
public class TestSuite {
}
