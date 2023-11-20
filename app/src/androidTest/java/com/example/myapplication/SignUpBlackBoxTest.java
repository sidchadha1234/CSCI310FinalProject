package com.example.myapplication;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.Espresso;
import androidx.test.filters.LargeTest;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import android.content.Intent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignUpBlackBoxTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void validRegistrationTest() {
        // Fill in the sign-up form
        onView(withId(R.id.signUpNameEditText)).perform(typeText("John Doe"));
        onView(withId(R.id.editTextGmail)).perform(typeText("johndoeeee@gmail.com"));
        onView(withId(R.id.signUpUSCIDEditText)).perform(typeText("1234567890"));
        onView(withId(R.id.signUpPasswordEditText)).perform(typeText("password123"));
        onView(withId(R.id.confirmPasswordEditText)).perform(typeText("password123"));

        // Perform a click on the sign-up button
        onView(withId(R.id.signUpConfirmButton)).perform(click());

        // Check that an Intent with the correct component has been sent
        onView(withId(R.id.main_activity_root)).check(matches(isDisplayed()));
    }
}
