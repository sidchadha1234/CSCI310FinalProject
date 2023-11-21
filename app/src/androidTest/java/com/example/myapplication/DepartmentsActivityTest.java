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
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4.class)
public class DepartmentsActivityTest {

    @Before
    public void setUp() {
        // Additional setup if needed
    }

    @Test
    public void testDepartmentCoursesDisplay() {
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
            Thread.sleep(5000); // waits for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Navigate to DepartmentsActivity
        onView(withId(R.id.buttonDepartments)).perform(click());

        // Verify the department is Computer Science
        onView(withId(R.id.spinnerDepartments)).check(matches(withSpinnerText(is("Computer Science"))));

        // Verify the list of courses
        String[] courses = {"Introduction to Programming", "Data Structures", "Algorithms", "Computer Networks", "Operating Systems", "Software Engineering"};
        for (String course : courses) {
            onData(equalTo(course))
                    .inAdapterView(withId(R.id.listViewCourses))
                    .check(matches(withText(course)));
        }
    }
}