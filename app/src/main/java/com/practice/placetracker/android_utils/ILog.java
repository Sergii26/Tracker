package com.practice.placetracker.android_utils;

public interface ILog {
    Logger log(String message);
    void withCause(Exception cause);
}
