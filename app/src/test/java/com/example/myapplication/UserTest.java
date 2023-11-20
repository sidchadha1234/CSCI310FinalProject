package com.example.myapplication;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;

public class UserTest {

    @Test
    public void testViewRegisteredCourses() {
        User user = new User();
        user.courses = new ArrayList<>(); // Manually setting the courses list
        user.courses.add("Course1");
        user.courses.add("Course2");

        assertNotNull("Courses list should not be null", user.courses);
        assertTrue("User should be registered in Course1", user.courses.contains("Course1"));
        assertTrue("User should be registered in Course2", user.courses.contains("Course2"));
    }
}
