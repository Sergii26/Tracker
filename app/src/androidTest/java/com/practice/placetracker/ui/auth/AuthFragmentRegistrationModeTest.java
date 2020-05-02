package com.practice.placetracker.ui.auth;

import com.practice.placetracker.R;
import com.practice.placetracker.rule.OpenLoginFragmentTestRule;
import com.practice.placetracker.rule.OpenRegistrationFragmentTestRule;
import com.practice.placetracker.ui.tracker.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

public class AuthFragmentRegistrationModeTest {

    @Rule
    public OpenRegistrationFragmentTestRule<MainActivity> mActivityRule =
            new OpenRegistrationFragmentTestRule<>(MainActivity.class);

    @Test
    public void viewsAreShownTest(){
        onView(allOf(withId(R.id.btnLogin), withText(R.string.label_registration))).check(matches(isDisplayed()));
        onView(withId(R.id.etEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()));
    }

}
