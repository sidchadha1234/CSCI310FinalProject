package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RateActivity extends AppCompatActivity {
    EditText workloadRatingEditText;
    EditText courseScoreEditText;
    CheckBox checksAttendanceCheckBox;
    CheckBox allowsLateSubmissionCheckBox;
    EditText commentsEditText;
    Button submitRatingButton;
    DatabaseReference mDatabase;

    private String departmentName;
    private String courseName;

    private TextView rateStatusTV;

    private String className;

    //private boolean preRated;

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
        rateStatusTV = findViewById(R.id.rateStatus);

        Intent intent = getIntent();
        departmentName = intent.getStringExtra("DEPARTMENT_NAME");
        courseName = intent.getStringExtra("COURSE_NAME");

        submitRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rateStatusTV.setText("Rated: Yes");
                submitRating(departmentName, courseName);

            }
        });
    }


    void submitRating(String departmentName, String courseName) {
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
            if (currentUser != null) {
                String userId = currentUser.getUid();
                DatabaseReference ratingsRef = mDatabase.child(departmentName).child(courseName).child("ratings");
                ratingsRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(RateActivity.this, "You have already rated this course", Toast.LENGTH_SHORT).show();
                        } else {
                            // Fallback to "Anonymous" if name not found
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("students").child(userId);
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String username = dataSnapshot.child("name").getValue(String.class);
                                    if (username == null) username = "Anonymous";

                                    // Create and save the rating instance with userId
//                                    rateStatusTV.setText("Rated: Yes");
                                    //preRated = true;
                                    CourseRatingInstance rating = new CourseRatingInstance(workloadRating, courseScore, checksAttendance, allowsLateSubmission, comments, username, departmentName, courseName, userId);
                                    saveRatingToFirebase(rating, departmentName, courseName);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(RateActivity.this, "Failed to fetch user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(RateActivity.this, "Failed to check existing ratings: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers for ratings", Toast.LENGTH_SHORT).show();
        }
    }



    private void saveRatingToFirebase(CourseRatingInstance rating, String departmentName, String courseName) {
        DatabaseReference ratingsRef = mDatabase.child(departmentName).child(courseName).child("ratings");
        DatabaseReference newRatingRef = ratingsRef.push(); // This creates a new unique key

        // Set the unique key to the rating object
        rating.setFirebaseKey(newRatingRef.getKey());

        // save entire object with the key to Firebase
        newRatingRef.setValue(rating)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RateActivity.this, "Rating submitted successfully!", Toast.LENGTH_SHORT).show();
                    rateStatusTV.setText("Rated Just Now: Yes"); // UI update moved here
                    // Other success handling logic
                })
                .addOnFailureListener(e -> Toast.makeText(RateActivity.this, "Failed to submit rating: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public boolean validateInput() {
        try {
            int workloadRating = Integer.parseInt(workloadRatingEditText.getText().toString());
            int courseScore = Integer.parseInt(courseScoreEditText.getText().toString());
            return workloadRating >= 1 && workloadRating <= 10 && courseScore >= 1 && courseScore <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
