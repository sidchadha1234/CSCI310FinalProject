package com.example.myapplication;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.Espresso;
import androidx.test.filters.LargeTest;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LogInBlackBoxTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void validLoginTest() {
        // Enter the USC ID
        onView(withId(R.id.loginUSCIDEditText)).perform(typeText("johndoeee@gmail.com"));

        // Enter the password
        onView(withId(R.id.loginPasswordEditText)).perform(typeText("password123"));

        // Close the soft keyboard
        Espresso.closeSoftKeyboard();

        // Scroll to the login button if necessary

        // Perform a click on the login button
        onView(withId(R.id.loginButton)).perform(click());

        // Check for a view that should be displayed after successful login
        onView(withId(R.id.textViewTitle)).check(matches(isDisplayed()));
    }
}
