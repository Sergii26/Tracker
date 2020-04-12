package com.practice.placetracker.ui.initial;

import android.text.TextUtils;

import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.model.prefs.Prefs;
import com.practice.placetracker.ui.arch.MvpPresenter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;

public class InitialPresenter extends MvpPresenter<InitialContract.View> implements InitialContract.Presenter {
    private final ILog logger = Logger.withTag("MyLog");

    private final Prefs prefs;

    InitialPresenter(Prefs prefs) {
        this.prefs = prefs;
    }

    @Override
    public void checkAuth() {
        onStopDisposable.add(Single.just(!TextUtils.isEmpty(prefs.getEmail()))
                // timeout to show an advertisement to user))
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isLoggedIn) throws Exception {
                        logger.log("InitialPresenter in checkAuth() email = " + prefs.getEmail());
                        if (InitialPresenter.this.hasView()) {
                            if (isLoggedIn) {
                                view.goToMainScreen();
                            } else {
                                view.enableButtons();
                            }
                        }
                    }
                }));
    }
}

