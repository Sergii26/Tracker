package com.practice.placetracker.ui.auth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding3.widget.RxTextView;
import com.jakewharton.rxbinding3.widget.TextViewAfterTextChangeEvent;
import com.practice.placetracker.R;
import com.practice.placetracker.model.logger.ILog;
import com.practice.placetracker.model.logger.Logger;
import com.practice.placetracker.ui.arch.fragments.MvpFragment;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

import static com.practice.placetracker.ui.auth.FragmentIndication.KEY_INDICATOR;

public class AuthFragment extends MvpFragment<AuthContract.Presenter, AuthContract.Host>
        implements AuthContract.View, android.view.View.OnClickListener {

    private final ILog logger = Logger.withTag("MyLog");

    private Unbinder unbinder;

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;

    private AuthContract.Presenter presenter;

    public static AuthFragment newInstance(int mode) {
        final Bundle b = new Bundle();
        b.putInt(KEY_INDICATOR, mode);
        final AuthFragment f = new AuthFragment();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        logger.log("AuthFragment in onCreate() mode = " + getArguments().getInt(KEY_INDICATOR));
        presenter = AuthFragmentInjector.injectPresenter(getArguments().getInt(KEY_INDICATOR));
    }

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_authorization, container, false);
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        final MenuItem menuItem = menu.findItem(R.id.action_log_out);
        menuItem.setVisible(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(android.view.View v) {
        if (v.getId() == R.id.btnLogin) {
            logger.log("AuthFragment in onClick() btnLogin branch");
            presenter.onButtonClick(etEmail.getText().toString(), etPassword.getText().toString());
            hideKeyboard(Objects.requireNonNull(getActivity()), v);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getPresenter().setupSubscriptions();
    }

    @Override
    public void openLocationFragment() {
        logger.log("AuthFragment in openLocationFragment()");
        // keyboard closing is replaced to onClick method

        if (hasCallBack()) {
            getCallBack().showLocationFragment();
        }
    }

    @Override
    public Observable<String> getPasswordObservable() {
        logger.log("AuthFragment in getPasswordObservable()");
        return RxTextView.afterTextChangeEvents(etPassword)
                .filter(event -> event.component2() != null)
                .map(event -> event.component2().toString());
    }

    @Override
    public Observable<String> getEmailObservable() {
        logger.log("AuthFragment in getEmailObservable()");
        return RxTextView.afterTextChangeEvents(etEmail)
                .filter(event -> event.component2() != null)
                .map(event -> event.component2().toString());
    }

    @Override
    public void setPasswordError(String errorMsg) {
        etPassword.setError(errorMsg);
    }

    @Override
    public void setUsernameError(String errorMsg) {
        etEmail.setError(errorMsg);
    }

    @Override
    public void sentRequestButtonEnabled(Boolean isEnabled) {
        btnLogin.setEnabled(isEnabled);
    }

    @Override
    public void setButtonText(String txt) {
        btnLogin.setText(txt);
    }

    @Override
    protected AuthContract.Presenter getPresenter() {
        return presenter;
    }

    private void hideKeyboard(Activity activity, View v) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}

