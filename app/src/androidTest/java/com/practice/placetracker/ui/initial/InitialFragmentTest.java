package com.practice.placetracker.ui.initial;

import com.practice.placetracker.R;
import com.practice.placetracker.rule.OpenInitialFragmentTestRule;
import com.practice.placetracker.ui.tracker.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.matcher.ViewMatchers;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

public class InitialFragmentTest {
    @Rule
    public OpenInitialFragmentTestRule<MainActivity> mActivityRule = new OpenInitialFragmentTestRule<>(MainActivity.class);

    @Test
    public void viewsIsDisplayedTest(){
        onView(ViewMatchers.withId(R.id.btnLoginFragment)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.btnRegistrationFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void loginFragmentOpenedTest(){
        onView(ViewMatchers.withId(R.id.btnLoginFragment)).perform(click());
        onView(allOf(withId(R.id.btnLogin), withText("Login")))
                .check(matches(isDisplayed()));
    }

    @Test
    public void registrationFragmentOpenedTest(){
        onView(ViewMatchers.withId(R.id.btnRegistrationFragment)).perform(click());
        onView(allOf(withId(R.id.btnLogin), withText("Registration")))
                .check(matches(isDisplayed()));
    }
}
