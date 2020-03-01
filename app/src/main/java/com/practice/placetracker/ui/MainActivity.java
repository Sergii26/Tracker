package com.practice.placetracker.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.practice.placetracker.R;
import com.practice.placetracker.android_utils.ILog;
import com.practice.placetracker.android_utils.Logger;
import com.practice.placetracker.ui.initial_fragment.InitialFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements FragmentChanger {

    private final ILog logger = Logger.withTag("MyLog");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getFragments().size() == 0) {
            Fragment fragment = InitialFragment.newInstance();

            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void openFragment(Fragment fragment, Boolean addToBackStack) {
        logger.log("MainActivity in openFragment()");
        FragmentTransaction transaction = null;
        if (getFragmentManager() != null) {
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);

            if(addToBackStack){
                transaction.addToBackStack(null);
            } else {
                getSupportFragmentManager().popBackStack();
            }

            transaction.commit();
        }

    }
}
