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

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;

//add ids to get this working
@RunWith(AndroidJUnit4.class)
public class checkRegisteredClassesAndUsers {


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

        // Enter the password
        onView(withId(R.id.loginPasswordEditText)).perform(typeText("siddy@924"));

        // Close the soft keyboard
        Espresso.closeSoftKeyboard();

        // Scroll to the login button if necessary

        // Perform a click on the login button
// Perform a click on the login button
        onView(withId(R.id.loginButton)).perform(click());

// Wait for the HomePageActivity to load
        try {
            Thread.sleep(2000); // waits for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

// Click the button to navigate to registered classes
        onView(withId(R.id.buttonMessages)).perform(click());

        try {
            Thread.sleep(2000); // waits for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.lv_registered_classes)).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.textViewUser), withText(containsString("Software Engineering"))))
                .check(matches(isDisplayed()));

// Check if "test eight" is listed as a shared user
        onView(allOf(withId(R.id.textViewUser), withText("test eight")))
                .check(matches(isDisplayed()));
    }

}
