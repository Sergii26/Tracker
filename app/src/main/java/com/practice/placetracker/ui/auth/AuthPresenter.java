package com.practice.placetracker.ui.auth;

import android.text.TextUtils;

import com.google.common.base.Optional;
import com.practice.placetracker.R;
import com.practice.placetracker.model.network.Result;
import com.practice.placetracker.model.network.auth.AuthNetwork;
import com.practice.placetracker.model.prefs.Prefs;
import com.practice.placetracker.ui.arch.MvpPresenter;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AuthPresenter extends MvpPresenter<AuthContract.View> implements AuthContract.Presenter {

    private final int mode;
    private final AuthNetwork network;
    private final Prefs prefs;

    AuthPresenter(AuthNetwork network, Prefs prefs, int mode) {
        this.network = network;
        this.prefs = prefs;
        this.mode = mode;
    }

    @Override
    public void onButtonClick(String email, String password) {
        final Single<Result<Boolean>> request;
        switch (mode) {
            case FragmentIndication.REGISTRATION_MODE:
                request = network.registerNewUser(email, password);
                break;
            case FragmentIndication.LOGIN_MODE:
                request = network.signIn(email, password);
                break;
            default:
                request = null;
        }
        if (request != null) {
            onStopDisposable.add(request
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> {
                        if (hasView()) {
                            view.showProgress();
                        }
                    })
                    .doFinally(() -> {
                        if (hasView()) {
                            view.hideProgress();
                        }
                    })
                    .filter(result -> hasView())
                    .subscribe(result -> {
                        if (result.isFail()) {
                            view.showToast(view.getString(R.string.sign_in_or_registration_error));
                        } else {
                            prefs.putEmail(email);
                            view.openLocationFragment();
                        }
                    }));
        }
    }

    @Override
    public void setupSubscriptions() {
        if (!hasView()) {
            return;
        }
        view.setButtonText(mode == FragmentIndication.REGISTRATION_MODE ?
                view.getString(R.string.label_registration) : view.getString(R.string.label_login));

        onStopDisposable.add(Observable.combineLatest(
                view.getPasswordObservable()
                        .map(password -> password.length() >= 6 ? Optional.<String>absent() : Optional.fromNullable(view.getString(R.string.password_too_short)))
                        .doOnNext(optional -> {
                            if (hasView()) {
                                view.setPasswordError(optional.isPresent() ? optional.get() : null);
                            }
                        }).map(optional -> !optional.isPresent()),
                view.getEmailObservable()
                        .map(email -> {
                            if (hasView()) {
                                final String errorMsg = TextUtils.isEmpty(email) ? view.getString(R.string.username_is_empty) : null;
                                view.setUsernameError(errorMsg);
                            }
                            return !TextUtils.isEmpty(email);
                        }),
                (isPasswordValid, isEmailValid) -> isEmailValid && isPasswordValid)
                .filter(isParamsValid -> hasView())
                .subscribe(isParamsValid -> view.sentRequestButtonEnabled(isParamsValid)));
    }
}




