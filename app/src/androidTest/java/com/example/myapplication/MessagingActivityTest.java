package com.example.myapplication;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

import static androidx.test.espresso.matcher.ViewMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;

import android.content.Intent;
//add ids to get this working
@RunWith(AndroidJUnit4.class)
public class MessagingActivityTest {


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
            Thread.sleep(5000); // waits for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

// Click the button to navigate to registered classes
        onView(withId(R.id.buttonMessages)).perform(click());

        try {
            Thread.sleep(5000); // waits for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.lv_registered_classes)).check(matches(isDisplayed()));

        // Wait for the registered classes activity to load
        // Additional wait or assertions can be added here

        // Click the first message button (assuming it can be uniquely identified)
// Click on any message button


        onView(withId(R.id.lv_registered_classes)).perform(clickOnAnyMessageButton());


        try {
            Thread.sleep(5000); // waits for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.fab)).check(matches(isDisplayed()));

        // Enter a message in the input field
        onView(withId(R.id.input)).perform(typeText("Hello there!"), closeSoftKeyboard());

        // Click the send button
        onView(withId(R.id.fab)).perform(click());
        try {
            Thread.sleep(5000); // waits for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Verify that the message appears in the chat
        onView(withText("Hello there!")).check(matches(isDisplayed()));
    }
    private ViewAction clickOnAnyMessageButton() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(ListView.class));
            }

            @Override
            public String getDescription() {
                return "Click on any visible message button in the ListView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ListView listView = (ListView) view;
                for (int i = 0; i < listView.getChildCount(); i++) {
                    View itemView = listView.getChildAt(i);
                    Button messageButton = itemView.findViewById(R.id.buttonMessage);
                    if (messageButton != null && messageButton.getVisibility() == View.VISIBLE) {
                        messageButton.performClick();
                        return; // Stop after clicking the first visible message button
                    }
                }
            }
        };
    }

}
