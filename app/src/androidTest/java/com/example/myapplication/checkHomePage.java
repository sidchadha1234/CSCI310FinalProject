package com.example.myapplication;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class checkHomePage {

    @Before
    public void setUp() {
        // Additional setup if needed
    }

    @Test
    public void testAppNavigationAndMessageSending() {
        // Start at the login screen
        ActivityScenario.launch(MainActivity.class);

        // Input email and password
        onView(withId(R.id.loginUSCIDEditText)).perform(typeText("sidchadha1@gmail.com"));
        onView(withId(R.id.loginPasswordEditText)).perform(typeText("siddy@924"));

        // Close the soft keyboard
        Espresso.closeSoftKeyboard();

        // Perform a click on the login button
        onView(withId(R.id.loginButton)).perform(click());

        // Wait for the HomePageActivity to load
        try {
            Thread.sleep(5000); // waits for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the Profile button displays the correct text
        onView(withId(R.id.buttonProfile)).check(matches(withText("Profile")));

        // Check if the Departments button displays the correct text
        onView(withId(R.id.buttonDepartments)).check(matches(withText("View Classes")));

        // Check if the Messages button displays the correct text
        onView(withId(R.id.buttonMessages)).check(matches(withText("View Registered Classes")));


    }
}