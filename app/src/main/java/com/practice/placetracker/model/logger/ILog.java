package com.practice.placetracker.model.logger;

public interface ILog {
    Logger log(String message);
    void withCause(Exception cause);
}
