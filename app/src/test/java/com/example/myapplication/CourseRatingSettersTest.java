package com.example.myapplication;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CourseRatingSettersTest {

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

        // Test setters
        courseRating.setWorkloadRating(4);
        assertEquals("Updated workloadRating should be 4", Integer.valueOf(4), courseRating.getWorkloadRating());

        courseRating.setCourseScore(85);
        assertEquals("Updated courseScore should be 85", Integer.valueOf(85), courseRating.getCourseScore());

        courseRating.setChecksAttendance(false);
        assertEquals("Updated checksAttendance should be false", false, courseRating.isChecksAttendance());

        courseRating.setAllowsLateSubmission(true);
        assertEquals("Updated allowsLateSubmission should be true", true, courseRating.isAllowsLateSubmission());

        courseRating.setComments("Could be better");
        assertEquals("Updated comments should be 'Could be better'", "Could be better", courseRating.getComments());

        courseRating.setUsername("Jane Smith");
        assertEquals("Updated username should be 'Jane Smith'", "Jane Smith", courseRating.getUsername());

        courseRating.setDepartmentName("Mathematics");
        assertEquals("Updated departmentName should be 'Mathematics'", "Mathematics", courseRating.getDepartmentName());

        courseRating.setCourseName("MATH101");
        assertEquals("Updated courseName should be 'MATH101'", "MATH101", courseRating.getCourseName());

        courseRating.setUserId("user456");
        assertEquals("Updated userId should be 'user456'", "user456", courseRating.getUserId());
    }
}
