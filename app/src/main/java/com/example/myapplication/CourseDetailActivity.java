package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseDetailActivity extends AppCompatActivity {

    private TextView textViewDescription;
    private TextView textViewUnits;
    private TextView textViewTime;
    private TextView textViewLocation;
    private TextView textViewSection;
    private TextView textViewDays;
    private TextView textViewInstructor;
    private TextView textViewEnrolled; // TextView to show the number of enrolled students
    private Button buttonRegisterCourse;
    private DatabaseReference mDatabase; // Firebase database reference
    private String studentUid; // UID of the logged-in user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // Initialize TextViews
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewUnits = findViewById(R.id.textViewUnits);
        textViewTime = findViewById(R.id.textViewTime);
        textViewLocation = findViewById(R.id.textViewLocation);
        textViewSection = findViewById(R.id.textViewSection);
        textViewDays = findViewById(R.id.textViewDays);
        textViewInstructor = findViewById(R.id.textViewInstructor);
        textViewEnrolled = findViewById(R.id.textViewEnrolled); // Initialize the enrolled TextView
        buttonRegisterCourse = findViewById(R.id.buttonRegisterCourse);

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get the course name and department name passed from the previous activity
        String courseName = getIntent().getStringExtra("COURSE_NAME");
        String departmentName = getIntent().getStringExtra("DEPARTMENT_NAME");

        // TODO: Retrieve the studentUid from the FirebaseAuth instance or pass it through the intent
        studentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Load the current number of enrolled students
        loadEnrolledCount(courseName, departmentName);

        buttonRegisterCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerForCourse(courseName, departmentName, studentUid);
            }
        });
    }

    private void loadEnrolledCount(String courseName, String departmentName) {
        DatabaseReference enrolledRef = mDatabase.child(departmentName).child(courseName).child("enrolled");
        enrolledRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer enrolled = dataSnapshot.getValue(Integer.class);
                if (enrolled != null) {
                    textViewEnrolled.setText("Enrolled: " + enrolled);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CourseDetailActivity.this, "Failed to load enrolled count.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerForCourse(String courseName, String departmentName, String studentUid) {
        // Reference to the course in the database
        DatabaseReference courseRef = mDatabase.child(departmentName).child(courseName);

        courseRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Course course = mutableData.getValue(Course.class);
                if (course == null) {
                    return Transaction.success(mutableData);
                }

                // Check if the student is already enrolled
                Map<String, Boolean> students = course.getStudents();
                if (students == null) {
                    students = new HashMap<>();
                }
                if (students.containsKey(studentUid)) {
                    // Student is already enrolled
                    return Transaction.abort();
                }

                if (course.getEnrolled() < course.getCapacity()) {
                    // Increment the number of students enrolled
                    course.setEnrolled(course.getEnrolled() + 1);
                    // Add the student's UID to the list of enrolled students
                    students.put(studentUid, true);
                    course.setStudents(students);
                    mutableData.setValue(course);
                } else {
                    // If the class is full, we abort the transaction
                    return Transaction.abort();
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                if (committed) {
                    Toast.makeText(CourseDetailActivity.this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                    // Update the enrolled TextView
                    Course updatedCourse = dataSnapshot.getValue(Course.class);
                    if (updatedCourse != null) {
                        textViewEnrolled.setText("Enrolled: " + updatedCourse.getEnrolled());
                    }
                    // Update the user's course list
                    updateUserCourseList(courseName, departmentName);
                } else {
                    if (databaseError != null && databaseError.getCode() == DatabaseError.PERMISSION_DENIED) {
                        Toast.makeText(CourseDetailActivity.this, "You are already registered for this course.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CourseDetailActivity.this, "Registration failed, class may be full.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void updateUserCourseList(String courseName, String departmentName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference userRef = mDatabase.child("students").child(user.getUid()).child("courses");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> courses = dataSnapshot.getValue(new GenericTypeIndicator<List<String>>() {});
                    if (courses == null) {
                        courses = new ArrayList<>();
                    }
                    courses.add(departmentName + " - " + courseName);
                    userRef.setValue(courses)
                            .addOnSuccessListener(aVoid -> Log.d("UpdateCourseList", "User's course list updated."))
                            .addOnFailureListener(e -> Log.d("UpdateCourseList", "Failed to update user's course list.", e));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("UpdateCourseList", "Failed to read user's course list.", databaseError.toException());
                }
            });
        } else {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
        }
    }
}

