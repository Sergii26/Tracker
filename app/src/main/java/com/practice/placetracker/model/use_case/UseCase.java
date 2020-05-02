package com.practice.placetracker.model.use_case;

public interface UseCase<Params, Result> {

    public Result execute(Params params);
}
