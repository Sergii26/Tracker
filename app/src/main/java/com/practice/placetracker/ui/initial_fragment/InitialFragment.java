package com.practice.placetracker.ui.initial_fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;

import com.practice.placetracker.R;
import com.practice.placetracker.android_utils.ILog;
import com.practice.placetracker.android_utils.Logger;
import com.practice.placetracker.ui.FragmentChanger;
import com.practice.placetracker.ui.MainActivity;
import com.practice.placetracker.ui.authorization_fragment.AuthorizationFragment;
import com.practice.placetracker.ui.authorization_fragment.FragmentIndication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InitialFragment extends Fragment implements android.view.View.OnClickListener, InitialContract.View {

    private final ILog logger = Logger.withTag("MyLog");

    private Unbinder unbinder;

    @BindView(R.id.btnLoginFragment)
    Button btnLoginFragment;

    @BindView(R.id.btnRegistrationFragment)
    Button btnRegistrationFragment;

    private InitialContract.Presenter presenter;

    public InitialFragment() {
    }

    public static InitialFragment newInstance() {
        return new InitialFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        presenter = InitialFragmentInjector.injectPresenter(this);
    }

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foreground, container, false);
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(android.view.View v) {
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
        logger.log("InitialFragment in openRegistrationFragment()");
        AuthorizationFragment fragment = AuthorizationFragment.newInstance(FragmentIndication.REGISTRATION_MODE);
        ((FragmentChanger)getActivity()).openFragment(fragment, true);
    }

    @Override
    public void openLoginFragment() {
        logger.log("InitialFragment on openLoginFragment()");
        Fragment fragment = AuthorizationFragment.newInstance(FragmentIndication.LOGIN_MODE);
        ((FragmentChanger)getActivity()).openFragment(fragment, true);
    }


}
