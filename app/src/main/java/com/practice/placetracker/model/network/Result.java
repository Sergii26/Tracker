package com.practice.placetracker.model.network;

public class Result<T> {
    private final T data;
    private final Throwable error;

    public Result(T data) {
        this.data = data;
        error = null;
    }

    public Result(Throwable error) {
        this.error = error;
        data = null;
    }

    public boolean isFail() {
        return error != null;
    }

    public T getData() {
        return data;
    }

    public Throwable getError() {
        return error;
    }
}

