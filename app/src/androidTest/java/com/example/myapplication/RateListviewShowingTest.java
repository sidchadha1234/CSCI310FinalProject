package com.example.myapplication;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

@RunWith(AndroidJUnit4.class)
public class RateListviewShowingTest {

    @Before
    public void setUp() {
        // Additional setup if needed
    }

    @Test
    public void testBusinessDepartmentCoursesDisplay() {
        // Start at the login screen
        ActivityScenario.launch(MainActivity.class);

        // Input email and password
        onView(withId(R.id.loginUSCIDEditText)).perform(typeText("mbshapir@gmail.com"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.loginPasswordEditText)).perform(typeText("testing"));
        Espresso.closeSoftKeyboard();
        // Perform a click on the login button
        onView(withId(R.id.loginButton)).perform(click());

        // Wait for the HomePageActivity to load

        // Navigate to DepartmentsActivity
        onView(withId(R.id.buttonMessages)).perform(click());

        // Verify the list of Business courses
        String[] courses = {
                "Communication - Public Speaking",
                "Computer Science - Introduction to Programming",
                "Computer Science - Data Structures",
                "Computer Science - Operating Systems",
                "Computer Science - Computer Networks"
        };

        //check if all desired courses are within courselist
        for (String expectedCourse : courses) {
            onData(hasToString(Matchers.containsString(expectedCourse))).inAdapterView(withId(R.id.lv_registered_classes)).check(matches(isDisplayed()));
        }
    }
}
