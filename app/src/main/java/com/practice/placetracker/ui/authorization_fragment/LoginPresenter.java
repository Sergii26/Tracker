package com.practice.placetracker.ui.authorization_fragment;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.practice.placetracker.R;

import androidx.annotation.NonNull;

public class LoginPresenter implements FragmentContract.BasePresenter {

    private FirebaseAuth mAuth;
    private Activity activity;
    private FragmentContract.BaseView view;

    public LoginPresenter(Activity activity, FragmentContract.BaseView view) {
        this.view = view;
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
    }

    public void onButtonClick(String email, String password){
        signIn(email, password);
    }

    public void signIn(String email, String password){
        Log.d("MyLog", "inLoginPresenter on signIn()");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("MyLog", "inLoginPresenter on signIn() signInWithEmail: success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            view.openLocationFragment(user.getEmail());
                        } else {
                            Log.d("MyLog", "inLoginPresenter on signIn() signInWithEmail: failure" + task.getException());
                            view.makeToast(view.getStringFromResources(R.string.sign_in_or_registration_error));
                        }
                    }
                });
    }

    public String getButtonText(){
        return view.getStringFromResources(R.string.label_login);
    }
}
