package com.practice.placetracker.model.dao;

import com.google.common.base.Optional;
import com.practice.placetracker.model.dao.map.MapDatabaseWorker;
import com.practice.placetracker.model.dao.map.MapRoomDao;
import com.practice.placetracker.model.logger.Logger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.TestObserver;

public class MapDatabaseWorkerTest {

    private MapDatabaseWorker dbWorker;
    private MapRoomDao mapRoomDao;
    private Logger logger;
    private List<TrackedLocationSchema> locations;

    @Before
    public void initWorker(){
        locations = new ArrayList<>();
        mapRoomDao = Mockito.mock(MapRoomDao.class);
        logger = Mockito.mock(Logger.class);
        dbWorker = new MapDatabaseWorker(mapRoomDao, logger);
        Mockito.when(mapRoomDao.getLastLocation("")).thenReturn(locations);
    }

    @Test
    public void getLastLocationTest_withNull(){
        TestObserver<Boolean> testObserver = dbWorker.getLastLocation("")
                .map(Optional::isPresent).test();
        testObserver.assertValue(false);
        testObserver.dispose();
    }

    @Test
    public void getLastLocationTest_withLocation(){
        locations.add(new TrackedLocationSchema(0, 0, 0, 0, 0, false, ""));
        TestObserver<Boolean> testObserver = dbWorker.getLastLocation("")
                .map(Optional::isPresent).test();
        testObserver.assertValue(true);
        testObserver.dispose();
    }

}
