package com.practice.placetracker.ui.authorization_fragment;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.practice.placetracker.R;

import androidx.annotation.NonNull;



public class RegistrationPresenter implements FragmentContract.BasePresenter {

    private FragmentContract.BaseView view;
    private Activity activity;
    private FirebaseAuth mAuth;

    public RegistrationPresenter(Activity activity, FragmentContract.BaseView view) {
        this.view = view;
        this.activity = activity;
        this.mAuth = FirebaseAuth.getInstance();
    }

    private boolean checkInputAndShowError(String email, String password){
        Log.d("MyLog", "inLoginPresenter on checkInputAndShowError()");
        boolean isCorrect = true;

        if(password.length() < 6){
            view.makeToast(view.getStringFromResources(R.string.wrong_password));
            isCorrect = false;
        }
        return isCorrect;
    }

    public void registerNewUser(String email, String password) {
        Log.d("MyLog", "inLoginPresenter on signNewUser()");
        if(!checkInputAndShowError(email, password)){
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("MyLog", "inLoginPresenter on signNewUser() createUserWithEmail: success");
                            view.openFragmentForSignIn();
                        } else {
                            Log.d("MyLog", "inLoginPresenter on signNewUser() createUserWithEmail: failure" + task.getException());
                            view.makeToast(view.getStringFromResources(R.string.sign_in_or_registration_error));
                        }
                    }
                });
    }

    @Override
    public void onButtonClick(String email, String password) {
        if(checkInputAndShowError(email, password)) {
            registerNewUser(email, password);
        }
    }

    public String getButtonText(){
        return view.getStringFromResources(R.string.label_registration);
    }

    public void showLoginFragment(){

    }
}
