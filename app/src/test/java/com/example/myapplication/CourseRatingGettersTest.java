package com.example.myapplication;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CourseRatingGettersTest {

    @Test
    public void testGettersAndSetters() {
        // Create a CourseRatingInstance instance with sample data
        CourseRatingInstance courseRating = new CourseRatingInstance(
                5, // workloadRating
                90, // courseScore
                true, // checksAttendance
                false, // allowsLateSubmission
                "Good course", // comments
                "John Doe", // username
                "Computer Science", // departmentName
                "CS101", // courseName
                "user123" // userId
        );

        // Test getters
        assertEquals("workloadRating should be 5", Integer.valueOf(5), courseRating.getWorkloadRating());
        assertEquals("courseScore should be 90", Integer.valueOf(90), courseRating.getCourseScore());
        assertEquals("checksAttendance should be true", true, courseRating.isChecksAttendance());
        assertEquals("allowsLateSubmission should be false", false, courseRating.isAllowsLateSubmission());
        assertEquals("comments should be 'Good course'", "Good course", courseRating.getComments());
        assertEquals("username should be 'John Doe'", "John Doe", courseRating.getUsername());
        assertEquals("departmentName should be 'Computer Science'", "Computer Science", courseRating.getDepartmentName());
        assertEquals("courseName should be 'CS101'", "CS101", courseRating.getCourseName());
        assertEquals("userId should be 'user123'", "user123", courseRating.getUserId());
    }
}
