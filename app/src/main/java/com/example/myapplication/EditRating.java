package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditRating extends AppCompatActivity {

    // UI elements
    private EditText workloadRatingEditText, courseScoreEditText, commentsEditText;
    private CheckBox checksAttendanceCheckBox, allowsLateSubmissionCheckBox;
    private Button updateRatingButton;

    // Firebase
    private DatabaseReference mDatabase;
    private String departmentName, courseName;
    private String ratingKey; // The key of the rating to be edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_rating); // Make sure to use the correct layout

        // Initialize Firebase and UI elements
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get the passed data
        Intent intent = getIntent();
        departmentName = intent.getStringExtra("DEPARTMENT_NAME");
        courseName = intent.getStringExtra("COURSE_NAME");
        ratingKey = intent.getStringExtra("RATING_KEY");


        //ui initialize
        workloadRatingEditText = findViewById(R.id.workloadRatingEditText);
        courseScoreEditText = findViewById(R.id.courseScoreEditText);
        checksAttendanceCheckBox = findViewById(R.id.checksAttendanceCheckBox);
        allowsLateSubmissionCheckBox = findViewById(R.id.allowsLateSubmissionCheckBox);
        commentsEditText = findViewById(R.id.commentsEditText);
        updateRatingButton = findViewById(R.id.updateRatingButton);

        //ratingKey = intent.getStringExtra("RATING_KEY"); // This key is crucial to identify which rating to update
        // loadExistingRating();

        updateRatingButton.setOnClickListener(v -> submitEditedRating());
    }


    private void submitEditedRating() {
        // First, get the values from the UI elements
        try {
            String workloadRatingStr = workloadRatingEditText.getText().toString();
            String courseScoreStr = courseScoreEditText.getText().toString();
            String comments = commentsEditText.getText().toString();
            boolean checksAttendance = checksAttendanceCheckBox.isChecked();
            boolean allowsLateSubmission = allowsLateSubmissionCheckBox.isChecked();

            // Convert the string values to the appropriate data types, if necessary
            int updatedWorkloadRating = 0;
            int updatedCourseScore = 0;
            try {
                updatedWorkloadRating = Integer.parseInt(workloadRatingStr);
                updatedCourseScore = Integer.parseInt(courseScoreStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid input. Please enter valid numbers for ratings", Toast.LENGTH_SHORT).show();
                return;
            }

            if (updatedWorkloadRating < 1 || updatedWorkloadRating > 10 || updatedCourseScore < 1 || updatedCourseScore > 10) {
                Toast.makeText(this, "Ratings must be between 1 and 10", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a map of values to update
            Map<String, Object> updatedValues = new HashMap<>();
            updatedValues.put("workloadRating", updatedWorkloadRating);
            updatedValues.put("courseScore", updatedCourseScore);
            updatedValues.put("comments", comments);
            updatedValues.put("checksAttendance", checksAttendance);
            updatedValues.put("allowsLateSubmission", allowsLateSubmission);

            // Update the existing rating in Firebase using the ratingKey
            DatabaseReference ratingRef = mDatabase.child(departmentName).child(courseName).child("ratings").child(ratingKey);
            ratingRef.updateChildren(updatedValues)
                    .addOnSuccessListener(aVoid -> Toast.makeText(EditRating.this, "Rating updated successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(EditRating.this, "Failed to update rating: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } catch(NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers for ratings", Toast.LENGTH_SHORT).show();
        }
    }
}