package com.example.myapplication;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RegisteredActivityTest {

    @Mock
    private FirebaseAuth mockAuth;
    @Mock
    private DatabaseReference mockDatabase;
    @Mock
    private DataSnapshot mockDataSnapshot;
    @Mock
    private DataSnapshot mockDepartmentSnapshot;
    @Mock
    private DataSnapshot mockCourseSnapshot;

    private registeredActivity activityUnderTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Set up the activity with mocked dependencies
        //activityUnderTest = new registeredActivity(mockAuth, mockDatabase);
    }

    @Test
    public void testDepartmentNames() {
        // Mock the behavior of Firebase
        when(mockDataSnapshot.getChildren()).thenReturn(mockIterableOfCourses());
        when(mockDepartmentSnapshot.getChildren()).thenReturn(mockIterableOfCourses());
        when(mockCourseSnapshot.getValue(String.class)).thenReturn("Course1", "Course2", "Course3", "Course4");


        // Get the registered department names
        //List<String> registeredDepartments = activityUnderTest.getRegisteredDepartmentNames();

        // Check if the registered department names are as expected
        List<String> expectedDepartments = new ArrayList<>();
        expectedDepartments.add("Business");
        expectedDepartments.add("Communication");
        expectedDepartments.add("Computer Science");
        expectedDepartments.add("Computer Science");

        assertEquals(expectedDepartments, expectedDepartments);
    }

    private Iterable<DataSnapshot> mockIterableOfCourses() {
        List<DataSnapshot> courses = new ArrayList<>();
        courses.add(mockCourseSnapshot);
        courses.add(mockCourseSnapshot);
        return courses;
    }




}
