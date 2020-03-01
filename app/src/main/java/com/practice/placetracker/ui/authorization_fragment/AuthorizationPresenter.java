package com.practice.placetracker.ui.authorization_fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.practice.placetracker.R;
import com.practice.placetracker.android_utils.ILog;
import com.practice.placetracker.android_utils.Logger;
import com.practice.placetracker.model.firebase_auth.IFirebaseAuthProvider;

import androidx.annotation.NonNull;



public class AuthorizationPresenter implements AuthorizationFragmentContract.Presenter {

    private final ILog logger = Logger.withTag("MyLog");

    private AuthorizationFragmentContract.View view;
    private FirebaseAuth mAuth;
    private int mode;

    public AuthorizationPresenter(AuthorizationFragmentContract.View view, IFirebaseAuthProvider firebaseAuthProvider, int mode) {
        this.view = view;
        this.mAuth = firebaseAuthProvider.getFirebaseAuthInstances();
        this.mode = mode;
    }

    private boolean checkPasswordAndShowError(String password){
        logger.log("AuthPresenter in checkPasswordAndShowError()");
        boolean isCorrect = true;

        if(password.length() < 6){
            view.makeToast(view.getStringFromResources(R.string.wrong_password));
            isCorrect = false;
        }
        return isCorrect;
    }

    public void registerNewUser(String email, String password) {
        logger.log("AuthPresenter in signNewUser()");
        if(!checkPasswordAndShowError(password)){
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(view.getAppActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            logger.log("AuthPresenter in registerNewUser() createUserWithEmail: success");
                            view.hideProgressDialog();
                            view.openFragmentForSignIn();
                        } else {
                            logger.log("AuthPresenter in registerNewUser() createUserWithEmail: failure" + task.getException());
                            view.hideProgressDialog();
                            view.makeToast(view.getStringFromResources(R.string.sign_in_or_registration_error));
                        }
                    }
                });
    }

    @Override
    public void onButtonClick(String email, String password) {
        switch(mode){
            case FragmentIndication.REGISTRATION_MODE:
                view.showProgressDialog(null);
                if(checkPasswordAndShowError(password)) {
                    registerNewUser(email, password);
                }
                break;
            case FragmentIndication.LOGIN_MODE:
                view.showProgressDialog(null);
                signIn(email, password);
                break;
        }

    }

    public String getButtonText(){
        return mode == FragmentIndication.REGISTRATION_MODE ?
                view.getStringFromResources(R.string.label_registration): view.getStringFromResources(R.string.label_login);
    }

    public void signIn(String email, String password){
        logger.log("AuthPresenter in signIn()");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(view.getAppActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            logger.log("AuthPresenter in signIn() signInWithEmail: success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userEmail = user.getEmail();
                            view.hideProgressDialog();
                            view.openLocationFragment(userEmail);
                        } else {
                            logger.log("AuthPresenter in signIn() signInWithEmail: failure" + task.getException());
                            view.hideProgressDialog();
                            view.makeToast(view.getStringFromResources(R.string.sign_in_or_registration_error));
                        }
                    }
                });
    }

}
