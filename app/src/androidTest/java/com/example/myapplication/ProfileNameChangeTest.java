package com.example.myapplication;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ProfileNameChangeTest {

    @Before
    public void setUp() {
        // Additional setup if needed
    }

    @Test
    public void testProfileNameChange() {
        // Start at the login screen
        ActivityScenario.launch(MainActivity.class);

        // Input email and password
        onView(withId(R.id.loginUSCIDEditText)).perform(typeText("pt@gmail.com"));
        onView(withId(R.id.loginPasswordEditText)).perform(typeText("1111111"));

        // Close the soft keyboard
        Espresso.closeSoftKeyboard();

        // Perform a click on the login button
        onView(withId(R.id.loginButton)).perform(click());

        // Wait for the HomePageActivity to load
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Navigate to the profile page
        onView(withId(R.id.buttonProfile)).perform(click());

        // Click on the name field and edit it
        onView(withId(R.id.editTextName)).perform(click(), clearText(), typeText("pt2"));

        // Close the soft keyboard
        Espresso.closeSoftKeyboard();

        // Save the profile or navigate away to trigger save
        // Assuming there's a save button or similar action
        onView(withId(R.id.buttonSave)).perform(click());

        // Return to the home page
        onView(withId(R.id.buttonReturnHome)).perform(click());

        // Navigate back to the profile page
        onView(withId(R.id.buttonProfile)).perform(click());

        // Verify the name is updated to "pt2"
        onView(withId(R.id.editTextName)).check(matches(withText("pt2")));
    }
}
