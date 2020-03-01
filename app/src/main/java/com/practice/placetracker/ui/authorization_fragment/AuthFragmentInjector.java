package com.practice.placetracker.ui.authorization_fragment;

import com.practice.placetracker.model.firebase_auth.FirebaseAuthProvider;

public class AuthFragmentInjector {
    static AuthorizationPresenter injectPresenter(AuthorizationFragmentContract.View view, int mode){
        return new AuthorizationPresenter(view, new FirebaseAuthProvider(), mode);
    }
}
