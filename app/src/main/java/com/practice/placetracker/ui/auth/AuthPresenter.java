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
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
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
                            view.showProgress("", view.getString(R.string.dialog_message));
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
                            view.showToast(R.string.sign_in_or_registration_error);
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
                        .map(new Function<String, Optional<String>>() {
                            @Override
                            public Optional<String> apply(String password) throws Exception {
                                return password.length() >= 6 ? Optional.<String>absent() : Optional.fromNullable(view.getString(R.string.password_too_short));
                            }
                        })
                        .doOnNext(new Consumer<Optional<String>>() {
                            @Override
                            public void accept(Optional<String> optional) throws Exception {
                                if (AuthPresenter.this.hasView()) {
                                    view.setPasswordError(optional.isPresent() ? optional.get() : null);
                                }
                            }
                        }).map(new Function<Optional<String>, Boolean>() {
                    @Override
                    public Boolean apply(Optional<String> optional) throws Exception {
                        return !optional.isPresent();
                    }
                }),
                view.getEmailObservable()
                        .map(email -> {
                            if (hasView()) {
                                final String errorMsg = email.isEmpty() ? view.getString(R.string.username_is_empty) : null;
                                view.setUsernameError(errorMsg);
                            }
                            return !email.isEmpty();
                        }),
                new BiFunction<Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean apply(Boolean isPasswordValid, Boolean isEmailValid) throws Exception {
                        return isEmailValid && isPasswordValid;
                    }
                })
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean isParamsValid) throws Exception {
                        return AuthPresenter.this.hasView();
                    }
                })
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isParamsValid) throws Exception {
                        view.sentRequestButtonEnabled(isParamsValid);
                    }
                }));
    }
}




