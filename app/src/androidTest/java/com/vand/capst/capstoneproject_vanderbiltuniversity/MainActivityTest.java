package com.vand.capst.capstoneproject_vanderbiltuniversity;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static android.support.test.espresso.Espresso.onView;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Sudeep.Pandey on 1/27/2018.
 *
 * The following test utilizes Espresso to perform actions on MainActivity screen and verifies if the result is accurate.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    //Espresso test used to carry out UI activities to verify state changes.
    @Test
    public void checkInitialUIState(){

        onView(withId(R.id.button)).check(matches(withText("Find")));
        onView(withId(R.id.button2)).check(matches(withText("View Result")));
        onView(withId(R.id.button)).check(matches(isEnabled()));
        onView(withId(R.id.button2)).check(matches(not(isEnabled())));

        onView(withId(R.id.button)).perform(click());

    }
}
