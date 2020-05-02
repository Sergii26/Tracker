package com.practice.placetracker.model.use_case.sender;

import com.practice.placetracker.model.dao.TrackedLocationSchema;
import com.practice.placetracker.model.dao.location.LocationDaoWorker;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.network.Result;
import com.practice.placetracker.model.network.location.LocationsNetwork;
import com.practice.placetracker.model.use_case.UseCase;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SendSavedLocationsUseCase implements UseCase<Void, Observable<Result<Boolean>>> {
    private final LocationsNetwork locationsNetwork;
    private final LocationDaoWorker dbWorker;
    private ILog logger;

    public SendSavedLocationsUseCase(LocationsNetwork locationsNetwork, LocationDaoWorker dbWorker, ILog logger) {
        this.locationsNetwork = locationsNetwork;
        this.dbWorker = dbWorker;
        this.logger = logger;
    }

    @Override
    public Observable<Result<Boolean>> execute(Void aVoid){
        return Observable.fromCallable(() -> {
            List<TrackedLocationSchema> locations = dbWorker.getLocationsBySent(false);
            for(int i = 0; i < locations.size(); i++){
                //for auto generation unique id in map database
                locations.get(i).setUniqueId(0);
            }
            return locations;
        })
                .flatMap((Function<List<TrackedLocationSchema>, ObservableSource<TrackedLocationSchema>>) trackedLocationSchemas ->
                        Observable.fromIterable(trackedLocationSchemas))
                .flatMap(trackedLocationSchema -> locationsNetwork.sendLocation(trackedLocationSchema).toObservable()
                        .flatMap(booleanResult -> {
                            if (booleanResult.isFail()) {
                                logger.log("SendSavedLocationsUseCase sendSavedLocations() result has error: " + booleanResult.getError().getMessage());
                                return Observable.just(new Result<>(false));
                            } else {
                                return dbWorker.deleteLocation(trackedLocationSchema)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnComplete(() -> logger.log("SendSavedLocationsUseCase sendSavedLocations() delete location - success"))
                                        .doOnError(throwable -> logger.log("SendSavedLocationsUseCase sendSavedLocations() delete location - failure"
                                                + throwable.getMessage()))
                                        .toObservable();
                            }
                        }));
    }



}
