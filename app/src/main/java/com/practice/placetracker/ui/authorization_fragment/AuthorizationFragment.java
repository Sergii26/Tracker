package com.practice.placetracker.ui.authorization_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.practice.placetracker.R;
import com.practice.placetracker.ui.location_fragment.LocationFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AuthorizationFragment extends Fragment implements FragmentContract.BaseView, View.OnClickListener {

    public static final String KEY_INDICATOR = "indicator";
    public static final String KEY_EMAIL = "userEmail";


    private Unbinder unbinder;

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;

    private FragmentContract.BasePresenter presenter;

    public AuthorizationFragment() {
    }

    public static AuthorizationFragment newInstance(int indicator) {
        final Bundle b = new Bundle();
        b.putInt(KEY_INDICATOR, indicator);
        final AuthorizationFragment f = new AuthorizationFragment();
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("MyLog", "AuthorizationFragment on onCreate() indication = " + getArguments().getInt(KEY_INDICATOR));
        presenter = getArguments().getInt(KEY_INDICATOR) == FragmentIndication.LOGIN_INDICATION ?
                new LoginPresenter(getActivity(),this) : new RegistrationPresenter(getActivity(), this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            Log.d("MyLog", "AuthorizationFragment in onClick() btnLogin branch");
            presenter.onButtonClick(etEmail.getText().toString(), etPassword.getText().toString());
        }
    }

    public void openLocationFragment(String userEmail) {
        Log.d("MyLog", "AuthorizationFragment in openLocationFragment()");
        LocationFragment locationFragment = LocationFragment.newInstance();
        final Bundle bundle = new Bundle();
        bundle.putString(KEY_EMAIL, userEmail);
        locationFragment.setArguments(bundle);
        FragmentTransaction transaction = null;
        if (getFragmentManager() != null) {
            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, locationFragment);
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            transaction.commit();
        } else {
            Log.d("MyLog", "AuthorizationFragment in openLocationFragment() getFragmentManager() = null");
        }
    }

    public void openFragmentForSignIn(){
        Log.d("MyLog", "AuthorizationFragment in openFragmentForSignIn()");
        AuthorizationFragment fragment = AuthorizationFragment.newInstance(FragmentIndication.LOGIN_INDICATION);
        FragmentTransaction transaction = null;
        if (getFragmentManager() != null) {
            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            Log.d("MyLog", "AuthorizationFragment in openFragmentForSignIn() getFragmentManager() = null");
        }
    }

    @Override
    public void makeToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public String getStringFromResources(int stringId) {
        Log.d("MyLog", "AuthorizationFragment in getStringFromResources()");
        return getString(stringId);
    }

//    public void testLogin() {
//        etEmail.setText("test@test.ua");
//        etPassword.setText("password");
//    }

}
