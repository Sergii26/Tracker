package com.practice.placetracker.ui.auth;

import com.practice.placetracker.R;
import com.practice.placetracker.rule.OpenLoginFragmentTestRule;
import com.practice.placetracker.ui.tracker.MainActivity;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

public class AuthFragmentLoginModeTest {

    @Rule
    public OpenLoginFragmentTestRule<MainActivity> mActivityRule =
            new OpenLoginFragmentTestRule<>(MainActivity.class);

    @Test
    public void viewsAreShownTest(){
        onView(allOf(withId(R.id.btnLogin), withText("Login"))).check(matches(isDisplayed()));
        onView(withId(R.id.etEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()));
    }

    @Test
    public void emailErrorIsShown(){
        onView(withId(R.id.etEmail)).perform(click());
        onView(withId(R.id.etEmail))
                .check(matches(hasErrorText(mActivityRule.getActivity().getString(R.string.username_is_empty))));
    }

    @Test
    public void emailErrorSkipped(){
        onView(withId(R.id.etEmail)).perform(click());
        onView(withId(R.id.etEmail))
                .check(matches(hasErrorText(mActivityRule.getActivity().getString(R.string.username_is_empty))));
        onView(withId(R.id.etEmail)).perform(typeText("email"));
        onView(withId(R.id.etEmail))
                .check(matches(not(hasErrorText(mActivityRule.getActivity().getString(R.string.username_is_empty)))));
    }

    @Test
    public void passwordErrorIsShown(){
        onView(withId(R.id.etPassword)).perform(click());
        onView(withId(R.id.etPassword))
                .check(matches(hasErrorText(mActivityRule.getActivity().getString(R.string.password_too_short))));
    }

    @Test
    public void passwordErrorSkipped(){
        onView(withId(R.id.etPassword)).perform(click());
        onView(withId(R.id.etPassword))
                .check(matches(hasErrorText(mActivityRule.getActivity().getString(R.string.password_too_short))));
        onView(withId(R.id.etPassword)).perform(typeText("password"));
        onView(withId(R.id.etPassword))
                .check(matches(not(hasErrorText(mActivityRule.getActivity().getString(R.string.password_too_short)))));
    }

    @Test
    public void progressDialogIsShown(){
        onView(withId(R.id.etEmail)).perform(typeText("test@test.ua"));
        onView(withId(R.id.etPassword)).perform(typeText("password"));
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withText(R.string.dialog_message)).inRoot(isDialog()).check(matches(isDisplayed()));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void locationFragmentIsShown(){
        onView(withId(R.id.etEmail)).perform(typeText("test@test.ua"));
        onView(withId(R.id.etPassword)).perform(typeText("password"));
        onView(withId(R.id.btnLogin)).perform(click());
        try {
            Thread.sleep(1300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.tvTime)).check(matches(isDisplayed()));
    }

    @Test
    public void errorIsShownWithInvalidUser(){
        onView(withId(R.id.etEmail)).perform(typeText("notExsistedUser@test.ua"));
        onView(withId(R.id.etPassword)).perform(typeText("password"));
        onView(withId(R.id.btnLogin)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText(R.string.sign_in_or_registration_error))
                .inRoot(withDecorView(Matchers.not(Matchers.is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }
}
