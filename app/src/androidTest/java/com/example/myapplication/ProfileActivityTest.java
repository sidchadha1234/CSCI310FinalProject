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

//add ids to get this working
@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest {

    @Before
    public void setUp() {
        // Additional setup if needed
    }

    @Test
    public void testProfileInformationDisplay() {
        // Start at the login screen
        ActivityScenario.launch(MainActivity.class);

        // Input email and password
        // Input email and password
        onView(withId(R.id.loginUSCIDEditText)).perform(typeText("pt@gmail.com"));

        // Enter the password
        onView(withId(R.id.loginPasswordEditText)).perform(typeText("1111111"));

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

        onView(withId(R.id.buttonProfile)).perform(click());

        // Check if the profile name is displayed and correct
        onView(withId(R.id.editTextName)).check(matches(withText("pt")));

        // Check if the USC ID is displayed and correct
        onView(withId(R.id.editTextUSCID)).check(matches(withText("1111111111")));

        // Check if the email is displayed and correct
        onView(withId(R.id.editTextEmail)).check(matches(withText("pt@gmail.com")));
    }

}
