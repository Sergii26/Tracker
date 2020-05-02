package com.practice.placetracker.ui.tracker;

import android.content.pm.ActivityInfo;

import com.practice.placetracker.R;
import com.practice.placetracker.rule.OpenLocationFragmentTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.provider.ProviderTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

public class LocationFragmentTest {

    @Rule
    public OpenLocationFragmentTestRule<MainActivity> mActivityRule =
            new OpenLocationFragmentTestRule<>(MainActivity.class);

    private void returnInitialState(){
        onView(withId(R.id.btnStop)).perform(click());
        onView(withId(R.id.action_log_out)).perform(click());
    }

    @Test
    public void viewsAreShown(){
        onView(withId(R.id.btnStart)).check(matches(isDisplayed()));
        onView(withId(R.id.btnStop)).check(matches(isDisplayed()));
        onView(withId(R.id.tvTime)).check(matches(isDisplayed()));
        onView(withId(R.id.tvLocationCountLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.tvLocationsByDistanceLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.tvLocationsByTimeLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.tvLocationsCount)).check(matches(isDisplayed()));
        onView(withId(R.id.tvLocationsByDistance)).check(matches(isDisplayed()));
        onView(withId(R.id.tvLocationsByTime)).check(matches(isDisplayed()));
    }

    @Test
    public void viewsAreShownWithInitialValue(){
        onView(withId(R.id.btnStart)).check(matches(isEnabled()));
        onView(withId(R.id.btnStop)).check(matches(not(isEnabled())));
        onView(withId(R.id.tvTime)).check(matches(withText(R.string.default_time)));

        onView(withId(R.id.tvLocationsCount)).check(matches(withText(R.string.number_zero)));
        onView(withId(R.id.tvLocationsByDistance)).check(matches(withText(R.string.number_zero)));
        onView(withId(R.id.tvLocationsByTime)).check(matches(withText(R.string.number_zero)));
    }

    @Test
    public void correctValueOnTwoLocations(){
        onView(withId(R.id.btnStart)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.btnStop)).check(matches(isEnabled()));
        onView(withId(R.id.btnStart)).check(matches(not(isEnabled())));
        onView(withId(R.id.tvTime)).check(matches(withText(R.string.timer_two_seconds)));

        onView(withId(R.id.tvLocationsCount)).check(matches(withText(R.string.number_two)));
        onView(withId(R.id.tvLocationsByDistance)).check(matches(withText(R.string.number_one)));
        onView(withId(R.id.tvLocationsByTime)).check(matches(withText(R.string.number_one)));

        returnInitialState();
    }

    @Test
    public void correctValuesOnTwoLocationsAfterChangeOrientation(){
        onView(withId(R.id.btnStart)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.btnStop)).check(matches(isEnabled()));
        onView(withId(R.id.btnStart)).check(matches(not(isEnabled())));
        onView(withId(R.id.tvTime)).check(matches(withText("0:00:02")));
        onView(withId(R.id.tvLocationsCount)).check(matches(withText("2")));
        onView(withId(R.id.tvLocationsByDistance)).check(matches(withText("1")));
        onView(withId(R.id.tvLocationsByTime)).check(matches(withText("1")));

        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        try {
            Thread.sleep(1300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.btnStop)).check(matches(isEnabled()));
        onView(withId(R.id.btnStart)).check(matches(not(isEnabled())));
        onView(withId(R.id.tvTime)).check(matches(withText("0:00:03")));
        onView(withId(R.id.tvLocationsCount)).check(matches(withText(R.string.number_two)));
        onView(withId(R.id.tvLocationsByDistance)).check(matches(withText(R.string.number_one)));
        onView(withId(R.id.tvLocationsByTime)).check(matches(withText(R.string.number_one)));

        returnInitialState();
    }

    @Test
    public void initialFragmentIsOpendAfterLogOut(){
        onView(withId(R.id.action_log_out)).perform(click());
        onView(withId(R.id.btnLoginFragment)).check(matches(isDisplayed()));
        onView(withId(R.id.btnRegistrationFragment)).check(matches(isDisplayed()));
    }


}
