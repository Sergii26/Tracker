package com.practice.placetracker.model.use_case;

import com.practice.placetracker.model.dao.TrackedLocationSchema;
import com.practice.placetracker.model.dao.location.LocationDaoWorker;
import com.practice.placetracker.model.dao.location.LocationDatabaseWorker;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.model.network.Result;
import com.practice.placetracker.model.network.location.FirebaseLocationsNetwork;
import com.practice.placetracker.model.network.location.LocationsNetwork;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SendSavedLocationsUseCaseTest {
    private LocationsNetwork locationsNetwork;
    private LocationDaoWorker dbWorker;
    private ILog logger;

    private SavedLocationsSender sender;

    @Before
    public void initSender(){
        locationsNetwork = Mockito.mock(FirebaseLocationsNetwork.class);
        dbWorker = Mockito.mock(LocationDatabaseWorker.class);
        logger = Mockito.mock(Logger.class);
        sender = new SendSavedLocationsUseCase(locationsNetwork, dbWorker, logger);
    }

    @Test
    public void sendSavedLocationsWithExceptionInResultOfSendingTest(){
        Result<Boolean> result = new Result<>(new Exception());
        List<TrackedLocationSchema> locations = new ArrayList<>();
        locations.add(new TrackedLocationSchema(0, 0, 0, 0, 0, false, ""));
        Mockito.when(dbWorker.getLocationsBySent(false)).thenReturn(locations);
        Mockito.when(dbWorker.deleteLocation(Mockito.any(TrackedLocationSchema.class))).thenReturn(Completable.complete());
        Mockito.when(locationsNetwork.sendLocation(Mockito.any(TrackedLocationSchema.class))).thenReturn(Single.just(result));

        sender.sendSavedLocations().subscribe(new Consumer<Result<Boolean>>() {
            @Override
            public void accept(Result<Boolean> booleanResult) throws Exception {
                assertFalse(booleanResult.getData());
            }
        });

        Mockito.verify(dbWorker, Mockito.times(1)).getLocationsBySent(false);
        Mockito.verify(locationsNetwork, Mockito.times(1)).sendLocation(Mockito.any(TrackedLocationSchema.class));
        Mockito.verifyNoMoreInteractions(dbWorker);
        Mockito.verifyNoMoreInteractions(locationsNetwork);
    }

    @Test
    public void sendSavedLocationsWithTrueInResultOfSendingTest(){
        Result<Boolean> result = new Result<>(true);
        List<TrackedLocationSchema> locations = new ArrayList<>();
        locations.add(new TrackedLocationSchema(0, 0, 0, 0, 0, false, ""));
        Mockito.when(dbWorker.getLocationsBySent(false)).thenReturn(locations);
        Mockito.when(dbWorker.deleteLocation(Mockito.any(TrackedLocationSchema.class))).thenReturn(Completable.complete());
        Mockito.when(locationsNetwork.sendLocation(Mockito.any(TrackedLocationSchema.class))).thenReturn(Single.just(result));
        sender.sendSavedLocations().subscribe();
        Mockito.verify(dbWorker, Mockito.times(1)).getLocationsBySent(false);
        Mockito.verify(locationsNetwork, Mockito.times(1)).sendLocation(Mockito.any(TrackedLocationSchema.class));
        Mockito.verify(dbWorker, Mockito.times(1)).deleteLocation(Mockito.any(TrackedLocationSchema.class));
        Mockito.verifyNoMoreInteractions(dbWorker);
        Mockito.verifyNoMoreInteractions(locationsNetwork);
    }

    @Test
    public void sendSavedLocationsWithSeveralLocationsForSendingTest(){
        Result<Boolean> result = new Result<>(true);
        List<TrackedLocationSchema> locations = new ArrayList<>();
        locations.add(new TrackedLocationSchema(0, 0, 0, 0, 0, false, ""));
        locations.add(new TrackedLocationSchema(0, 0, 0, 0, 0, false, ""));
        locations.add(new TrackedLocationSchema(0, 0, 0, 0, 0, false, ""));
        Mockito.when(dbWorker.getLocationsBySent(false)).thenReturn(locations);
        Mockito.when(dbWorker.deleteLocation(Mockito.any(TrackedLocationSchema.class))).thenReturn(Completable.complete());
        Mockito.when(locationsNetwork.sendLocation(Mockito.any(TrackedLocationSchema.class))).thenReturn(Single.just(result));
        sender.sendSavedLocations().subscribe();
        Mockito.verify(dbWorker, Mockito.times(1)).getLocationsBySent(false);
        Mockito.verify(locationsNetwork, Mockito.times(3)).sendLocation(Mockito.any(TrackedLocationSchema.class));
        Mockito.verify(dbWorker, Mockito.times(3)).deleteLocation(Mockito.any(TrackedLocationSchema.class));
        Mockito.verifyNoMoreInteractions(dbWorker);
        Mockito.verifyNoMoreInteractions(locationsNetwork);
    }
}
