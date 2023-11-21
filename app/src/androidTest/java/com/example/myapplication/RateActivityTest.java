package com.example.myapplication;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.Root;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
//import static org.hamcrest.Matchers.is;


import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;

import android.os.IBinder;
import android.view.WindowManager;


@RunWith(AndroidJUnit4.class)
public class RateActivityTest {

    @Before
    public void setUp() {
        // Additional setup if needed
    }

    @Test
    public void testValidRatingSubmission() {
        // Start at the login screen
        ActivityScenario.launch(MainActivity.class);

        // Input email and password
        onView(withId(R.id.loginUSCIDEditText)).perform(typeText("activityratetestuser@gmail.com"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.loginPasswordEditText)).perform(typeText("testing123"));
        Espresso.closeSoftKeyboard();

        // Perform a click on the login button
        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.buttonDepartments)).perform(click());

        onView(withId(R.id.spinnerDepartments)).perform(click());
        onData(is(anything())).atPosition(0).perform(click());

        onData(is(anything())).inAdapterView(withId(R.id.listViewCourses)).atPosition(5).perform(click());

        onView(withId(R.id.buttonSelectCourse)).perform(click());

        onView(withId(R.id.buttonRateClass)).perform(click());


        // Input valid rating details
        onView(withId(R.id.workloadRating)).perform(typeText("8"), closeSoftKeyboard());
        onView(withId(R.id.courseScore)).perform(typeText("7"), closeSoftKeyboard());
        onView(withId(R.id.checksAttendance)).perform(click());
        onView(withId(R.id.allowsLateSubmission)).perform(click());
        onView(withId(R.id.comments)).perform(typeText("Good course"), closeSoftKeyboard());

        // Submit the rating
        onView(withId(R.id.submitRating)).perform(click());

        onView(withId(R.id.rateStatus)).check(matches(withText("Rated Just Now: Yes")));

    }
}
