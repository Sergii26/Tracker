package com.practice.placetracker.model.network.auth;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.model.network.Result;

import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;


public class FirebaseAuthNetwork implements AuthNetwork {

    private final ILog logger = Logger.withTag("MyLog");
    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthNetwork() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean isUserLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    @Override
    public Single<Result<Boolean>> registerNewUser(String email, String password) {
        logger.log("FirebaseAuthNetwork in registerNewUser()");
        // register user and sign in him immediately
        return Single.fromCallable(new Callable<Result<Boolean>>() {
            @Override
            public Result<Boolean> call() throws Exception {
                try {
                    final AuthResult await = Tasks.await(firebaseAuth.createUserWithEmailAndPassword(email, password));
                    logger.log("FirebaseAuthNetwork in registerNewUser() createUserWithEmail: success");
                    return new Result<>(true);
                } catch (Throwable e) {
                    logger.log("FirebaseAuthNetwork in registerNewUser() createUserWithEmail: failure = " + e.getMessage());
                    return new Result<>(e);
                }
            }
        })
                .flatMap(new Function<Result<Boolean>, SingleSource<? extends Result<Boolean>>>() {
                    @Override
                    public SingleSource<? extends Result<Boolean>> apply(Result<Boolean> result) throws Exception {
                        if (result.isFail()) {
                            return Single.just(result);
                        } else {
                            return FirebaseAuthNetwork.this.signIn(email, password);
                        }
                    }
                });

    }

    @Override
    public Single<Result<Boolean>> signIn(String email, String password) {
        logger.log("FirebaseAuthNetwork in signIn()");
        return Single.fromCallable(new Callable<Result<Boolean>>() {
            @Override
            public Result<Boolean> call() throws Exception {
                try {
                    final AuthResult await = Tasks.await(firebaseAuth.signInWithEmailAndPassword(email, password));
                    logger.log("FirebaseAuthNetwork in signIn() signInWithEmail: success");
                    return new Result<>(true);
                } catch (Throwable e) {
                    logger.log("FirebaseAuthNetwork in signIn() signInWithEmail: failure" + e.getMessage());
                    return new Result<>(e);
                }
            }
        });

    }

    @Override
    public void logOut() {
        firebaseAuth.signOut();
    }
}


