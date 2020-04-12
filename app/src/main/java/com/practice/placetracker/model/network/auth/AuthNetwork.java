package com.practice.placetracker.model.network.auth;

import com.practice.placetracker.model.network.Result;

import io.reactivex.Single;

public interface AuthNetwork {
    boolean isUserLoggedIn();

    Single<Result<Boolean>> registerNewUser(String email, String password);

    Single<Result<Boolean>> signIn(String email, String password);

    void logOut();

}
