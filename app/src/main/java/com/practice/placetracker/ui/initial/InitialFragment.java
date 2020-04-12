package com.practice.placetracker.ui.initial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;

import com.practice.placetracker.R;
import com.practice.placetracker.ui.arch.fragments.MvpFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InitialFragment extends MvpFragment<InitialContract.Presenter, InitialContract.Host>
        implements android.view.View.OnClickListener, InitialContract.View {

    private Unbinder unbinder;

    @BindView(R.id.btnLoginFragment)
    Button btnLoginFragment;

    @BindView(R.id.btnRegistrationFragment)
    Button btnRegistrationFragment;

    private InitialContract.Presenter presenter;

    public static InitialFragment newInstance() {
        return new InitialFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        presenter = InitialFragmentInjector.injectPresenter();
    }

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_initial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        btnLoginFragment.setOnClickListener(this);
        btnRegistrationFragment.setOnClickListener(this);
    }


    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // why?
        final MenuItem menuItem = menu.findItem(R.id.action_log_out);
        menuItem.setVisible(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getPresenter().checkAuth();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(android.view.View v) {
        if (hasCallBack()) {
            switch (v.getId()) {
                case R.id.btnLoginFragment:
                    getCallBack().showLoginFragment();
                    break;
                case R.id.btnRegistrationFragment:
                    getCallBack().showRegistrationFragment();
                    break;
            }
        }
    }

    @Override
    protected InitialContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void enableButtons() {
        btnRegistrationFragment.setEnabled(true);
        btnLoginFragment.setEnabled(true);
    }

    @Override
    public void goToMainScreen() {
        if (hasCallBack()) {
            getCallBack().showLocationFragment();
        }
    }
}

