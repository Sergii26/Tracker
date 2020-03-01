package com.practice.placetracker.model.data.room;

import dagger.Component;

@Component(modules = {DatabaseWorkerModule.class})
public interface DatabaseWorkerComponent {
    DatabaseWorker provideDatabaseWorker();
}
