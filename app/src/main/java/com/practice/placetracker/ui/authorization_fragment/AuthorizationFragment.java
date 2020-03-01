package com.practice.placetracker.ui.authorization_fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.practice.placetracker.R;
import com.practice.placetracker.android_utils.ILog;
import com.practice.placetracker.android_utils.Logger;
import com.practice.placetracker.ui.FragmentChanger;
import com.practice.placetracker.ui.location_fragment.LocationFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.practice.placetracker.ui.authorization_fragment.FragmentIndication.KEY_INDICATOR;

public class AuthorizationFragment extends androidx.fragment.app.Fragment implements AuthorizationFragmentContract.View, android.view.View.OnClickListener {

    private final ILog logger = Logger.withTag("MyLog");

    private Unbinder unbinder;
    private ProgressDialog dialog;

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;

    private AuthorizationFragmentContract.Presenter presenter;

    public AuthorizationFragment() {
    }

    public static AuthorizationFragment newInstance(int mode) {
        final Bundle b = new Bundle();
        b.putInt(KEY_INDICATOR, mode);
        final AuthorizationFragment f = new AuthorizationFragment();
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        logger.log("AuthorizationFragment in onCreate() mode = " + getArguments().getInt(KEY_INDICATOR));
        presenter = AuthFragmentInjector.injectPresenter(this, getArguments().getInt(KEY_INDICATOR));
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        btnLogin.setText(presenter.getButtonText());
        btnLogin.setOnClickListener(this);
//        testLogin();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.action_log_out);
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
            logger.log("AuthorizationFragment in onClick() btnLogin branch");
            presenter.onButtonClick(etEmail.getText().toString(), etPassword.getText().toString());
        }
    }

    public void openLocationFragment(String userEmail) {
        logger.log("AuthorizationFragment in openLocationFragment()");
        Fragment fragment = LocationFragment.newInstance(userEmail);
        ((FragmentChanger)getActivity()).openFragment(fragment, false);
    }

    public void openFragmentForSignIn(){
        logger.log("AuthorizationFragment in openFragmentForSignIn()");
        Fragment fragment = AuthorizationFragment.newInstance(FragmentIndication.LOGIN_MODE);
        ((FragmentChanger)getActivity()).openFragment(fragment, true);
    }

    @Override
    public void makeToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public String getStringFromResources(int stringId) {
        logger.log("AuthorizationFragment in getStringFromResources()");
        return getString(stringId);
    }

//    public void testLogin() {
//        etEmail.setText("test@test.ua");
//        etPassword.setText("password");
//    }

    public Activity getAppActivity(){
        return getActivity();
    }

    public void showProgressDialog(String msg){
        dialog = new ProgressDialog(getActivity(), R.style.CustomDialogTheme);
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void hideProgressDialog(){
        dialog.cancel();
    }

}
