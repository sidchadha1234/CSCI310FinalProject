package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RateActivity extends AppCompatActivity {
    private EditText workloadRatingEditText;
    private EditText courseScoreEditText;
    private CheckBox checksAttendanceCheckBox;
    private CheckBox allowsLateSubmissionCheckBox;
    private EditText commentsEditText;
    private Button submitRatingButton;
    private DatabaseReference mDatabase;

    private String departmentName;
    private String courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_class_instance_input);

        //initialize database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        workloadRatingEditText = findViewById(R.id.workloadRating);
        courseScoreEditText = findViewById(R.id.courseScore);
        checksAttendanceCheckBox = findViewById(R.id.checksAttendance);
        allowsLateSubmissionCheckBox = findViewById(R.id.allowsLateSubmission);
        commentsEditText = findViewById(R.id.comments);
        submitRatingButton = findViewById(R.id.submitRating);

        Intent intent = getIntent();
        departmentName = intent.getStringExtra("DEPARTMENT_NAME");
        courseName = intent.getStringExtra("COURSE_NAME");

        submitRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRating(departmentName, courseName);
            }
        });
    }

    private void submitRating(String departmentName, String courseName) {
        try {
            int workloadRating = Integer.parseInt(workloadRatingEditText.getText().toString());
            int courseScore = Integer.parseInt(courseScoreEditText.getText().toString());

            if (workloadRating < 1 || workloadRating > 10 || courseScore < 1 || courseScore > 10) {
                Toast.makeText(this, "Ratings must be between 1 and 10", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean checksAttendance = checksAttendanceCheckBox.isChecked();
            boolean allowsLateSubmission = allowsLateSubmissionCheckBox.isChecked();
            String comments = commentsEditText.getText().toString();

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String username = currentUser != null ? currentUser.getDisplayName() : "Anonymous"; // Or another way you handle usernames

            // Create a CourseRating object
            CourseRatingInstance rating = new CourseRatingInstance(workloadRating, courseScore, checksAttendance, allowsLateSubmission, comments, username, departmentName, courseName);
            saveRatingToFirebase(rating, departmentName, courseName);
        }catch (NumberFormatException e){
            Toast.makeText(this, "Please enter valid numbers for ratings", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveRatingToFirebase(CourseRatingInstance rating, String departmentName, String courseName) {

        DatabaseReference ratingsRef = mDatabase.child("departments").child(departmentName).child("courses").child(courseName).child("ratings");

        // Push the new rating to the database
        ratingsRef.push().setValue(rating)
                .addOnSuccessListener(aVoid -> Toast.makeText(RateActivity.this, "Rating submitted successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(RateActivity.this, "Failed to submit rating: " + e.getMessage(), Toast.LENGTH_SHORT).show());

    }
}


//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//
//        String courseId = "the_course_id"; // Replace with actual course ID
//        databaseReference.child("courses").child(courseId).child("ratings").push().setValue(rating)
//                .addOnSuccessListener(aVoid -> {
//                    // Handle success, e.g., show a toast message
//                    Toast.makeText(RateActivity.this, "Rating submitted successfully!", Toast.LENGTH_SHORT).show();
//                })
//                .addOnFailureListener(e -> {
//                    // Handle failure, e.g., show an error message
//                    Toast.makeText(RateActivity.this, "Failed to submit rating: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });