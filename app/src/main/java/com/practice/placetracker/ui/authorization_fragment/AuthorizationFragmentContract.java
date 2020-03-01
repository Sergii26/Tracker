package com.practice.placetracker.ui.authorization_fragment;

import android.app.Activity;

public class AuthorizationFragmentContract {
    interface View {
        void openLocationFragment(String userEmail);
        void makeToast(String message);
        String getStringFromResources(int stringId);
        void openFragmentForSignIn();
        Activity getAppActivity();
        void showProgressDialog(String msg);
        void hideProgressDialog();
    }

    interface Presenter {
        void onButtonClick(String email, String password);
        String getButtonText();
    }
}
