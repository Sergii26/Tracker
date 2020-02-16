package com.practice.placetracker.ui.foreground_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.practice.placetracker.R;
import com.practice.placetracker.ui.authorization_fragment.AuthorizationFragment;
import com.practice.placetracker.ui.authorization_fragment.FragmentIndication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ForegroundFragment extends Fragment implements View.OnClickListener, ForegroundContract.ForegroundView {

    private Unbinder unbinder;

    @BindView(R.id.btnLoginFragment)
    Button btnLoginFragment;

    @BindView(R.id.btnRegistrationFragment)
    Button btnRegistrationFragment;

    private ForegroundContract.ForegroundBasePresenter presenter;

    public ForegroundFragment() {
    }

    public static ForegroundFragment newInstance() {
        return new ForegroundFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        presenter = new ForegroundPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foreground, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        btnLoginFragment.setOnClickListener(this);
        btnRegistrationFragment.setOnClickListener(this);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.action_log_out);
        menuItem.setVisible(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLoginFragment:
                presenter.showLoginFragment();
                break;
            case R.id.btnRegistrationFragment:
                presenter.showRegistrationFragment();
                break;
        }
    }

    public void openRegistrationFragment(){
        Log.d("MyLog", "ForegroundFragment on openRegistrationFragment()");
        AuthorizationFragment fragment = AuthorizationFragment.newInstance(FragmentIndication.REGISTRATION_INDICATION);
        FragmentTransaction transaction = null;
        if (getFragmentManager() != null) {
            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack("registration");
            transaction.commit();
        } else {
            Log.d("MyLog", "ForegroundFragment on openRegistrationFragment() getFragmentManager() = null");
        }
    }

    @Override
    public void openLoginFragment() {
        Log.d("MyLog", "ForegroundFragment on openForegroundFragment()");
        AuthorizationFragment fragment = AuthorizationFragment.newInstance(FragmentIndication.LOGIN_INDICATION);
        FragmentTransaction transaction = null;
        if (getFragmentManager() != null) {
            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack("login");
            transaction.commit();
        } else {
            Log.d("MyLog", "ForegroundFragment on openForegroundFragment() getFragmentManager() = null");
        }
    }
}
