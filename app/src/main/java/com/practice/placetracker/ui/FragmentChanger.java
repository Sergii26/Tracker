package com.practice.placetracker.ui;

import androidx.fragment.app.Fragment;

public interface FragmentChanger {
    void openFragment(Fragment fragment, Boolean addToBackStack);
}
