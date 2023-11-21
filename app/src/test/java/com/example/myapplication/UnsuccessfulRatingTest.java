package com.example.myapplication;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import android.text.Editable;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;

@RunWith(RobolectricTestRunner.class)
public class UnsuccessfulRatingTest {

    @Mock
    private EditText workloadRatingEditText;
    @Mock
    private EditText courseScoreEditText;

    private RateActivity rateActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        rateActivity = new RateActivity();
        rateActivity.workloadRatingEditText = workloadRatingEditText;
        rateActivity.courseScoreEditText = courseScoreEditText;

        Editable mockEditableWorkload = mock(Editable.class);
        Editable mockEditableCourseScore = mock(Editable.class);
        when(rateActivity.workloadRatingEditText.getText()).thenReturn(mockEditableWorkload);
        when(rateActivity.courseScoreEditText.getText()).thenReturn(mockEditableCourseScore);

        // Further stubbing to return specific strings for toString()
        when(mockEditableWorkload.toString()).thenReturn("11"); // You can adjust these values for different test cases
        when(mockEditableCourseScore.toString()).thenReturn("0");
    }

    @Test
    public void submitRating_withInvalidData_ShowsError() {
        // Simulate invalid input
        when(workloadRatingEditText.getText().toString()).thenReturn("11"); // Invalid workload rating
        when(courseScoreEditText.getText().toString()).thenReturn("0"); // Invalid course score

        assertFalse(rateActivity.validateInput()); // Expect the validation to fail
    }

}