package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
    private EditText workloadRatingEditText;
    private EditText courseScoreEditText;
    private CheckBox checksAttendanceCheckBox;
    private CheckBox allowsLateSubmissionCheckBox;
    private EditText commentsEditText;
    private Button submitRatingButton;
    private DatabaseReference mDatabase;

    private String departmentName;
    private String courseName;

    private String className;

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

//    private void submitRating(String departmentName, String courseName) {
//        try {
//            int workloadRating = Integer.parseInt(workloadRatingEditText.getText().toString());
//            int courseScore = Integer.parseInt(courseScoreEditText.getText().toString());
//
//            if (workloadRating < 1 || workloadRating > 10 || courseScore < 1 || courseScore > 10) {
//                Toast.makeText(this, "Ratings must be between 1 and 10", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            boolean checksAttendance = checksAttendanceCheckBox.isChecked();
//            boolean allowsLateSubmission = allowsLateSubmissionCheckBox.isChecked();
//            String comments = commentsEditText.getText().toString();
//
//            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//            if (currentUser != null) {
//                String userId = currentUser.getUid();
//                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("students").child(userId);
//                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        String username = dataSnapshot.child("name").getValue(String.class);
//                        if (username == null) username = "Anonymous"; // Fallback to "Anonymous" if name not found
//
//                        // Create and save the rating instance
//                        CourseRatingInstance rating = new CourseRatingInstance(workloadRating, courseScore, checksAttendance, allowsLateSubmission, comments, username, departmentName, courseName, userID);
//                        saveRatingToFirebase(rating, departmentName, courseName);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(RateActivity.this, "Failed to fetch user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } else {
//                Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
//            }
//        }catch (NumberFormatException e){
//            Toast.makeText(this, "Please enter valid numbers for ratings", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void saveRatingToFirebase(CourseRatingInstance rating, String departmentName, String courseName) {
        DatabaseReference ratingsRef = mDatabase.child(departmentName).child(courseName).child("ratings");
        DatabaseReference newRatingRef = ratingsRef.push(); // This creates a new unique key

        // Set the unique key to the rating object
        rating.setFirebaseKey(newRatingRef.getKey());

        // save entire object with the key to Firebase
        newRatingRef.setValue(rating)
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