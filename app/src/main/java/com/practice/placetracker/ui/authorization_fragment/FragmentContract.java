package com.practice.placetracker.ui.authorization_fragment;

public class FragmentContract {
    interface BaseView{
        void openLocationFragment(String userEmail);
        void makeToast(String message);
        String getStringFromResources(int stringId);
        void openFragmentForSignIn();
    }

    interface BasePresenter {
        void onButtonClick(String email, String password);
        String getButtonText();
    }
}
