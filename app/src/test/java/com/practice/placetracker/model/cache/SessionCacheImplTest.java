package com.practice.placetracker.model.cache;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.observers.TestObserver;

import static com.practice.placetracker.model.cache.SessionCache.TYPE_DISTANCE;
import static com.practice.placetracker.model.cache.SessionCache.TYPE_TIME;
import static org.junit.Assert.assertEquals;

public class SessionCacheImplTest {

    private SessionCache cache;

    @Before
    public void initCache(){
        cache = new SessionCacheImpl();
    }

    @Test
    public void updateSession_WithMapIndication(){
        TestObserver<Integer> observer = new TestObserver<>();
        cache.updateSession(TYPE_TIME);
        cache.getLocationsCountObservable().subscribe(observer);
        assertEquals(cache.getLocationCount(), 1);
        assertEquals(cache.getLocationsByDistance(), 0);
        assertEquals(cache.getLocationsByTime(), 1);
        observer.assertNoErrors();
        observer.assertValuesOnly(1);
        observer.dispose();
    }

    @Test
    public void updateSession_WithDistanceIndication(){
        TestObserver<Integer> observer = new TestObserver<>();
        cache.updateSession(TYPE_DISTANCE);
        cache.getLocationsCountObservable().subscribe(observer);
        assertEquals(cache.getLocationCount(), 1);
        assertEquals(cache.getLocationsByDistance(), 1);
        assertEquals(cache.getLocationsByTime(), 0);
        observer.assertNoErrors();
        observer.assertValuesOnly(1);
        observer.dispose();
    }
}
