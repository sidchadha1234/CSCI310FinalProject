package com.example.myapplication;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class CourseTest {

    @Test
    public void testViewUsersInSameCourse() {
        Course course = new Course();
        Map<String, Boolean> students = new HashMap<>();
        students.put("user1", true);
        students.put("user2", true);
        course.setStudents(students); // Manually setting the students map

        Map<String, Boolean> retrievedStudents = course.getStudents();

        assertNotNull("Students map should not be null", retrievedStudents);
        assertTrue("The course should contain user1", retrievedStudents.containsKey("user1"));
        assertTrue("The course should contain user2", retrievedStudents.containsKey("user2"));
    }
}
