package com.example.myapplication;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ViewRatingsActivity extends AppCompatActivity {
    private TextView courseNameTextView, averageWorkloadTextView, averageRatingTextView, averageAttendanceTextView, averageLateSubmissionTextView;
    private ListView ratingsListView;
    private DatabaseReference mDatabase;
    private String departmentName, courseName;
    private List<CourseRatingInstance> ratingsList;
    private AdapterForListview ratingsAdapter; // Assuming you have a custom adapter named RatingsAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_class_ratings);

        // Initialize views
        courseNameTextView = findViewById(R.id.courseNameTextView);
        averageWorkloadTextView = findViewById(R.id.averageWorkloadTextView);
        averageRatingTextView = findViewById(R.id.averageRatingTextView);
        averageAttendanceTextView = findViewById(R.id.averageAttendanceTextView);
        averageLateSubmissionTextView = findViewById(R.id.averageLateSubmissionTextView);
        ratingsListView = findViewById(R.id.ratingsListView);

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Retrieve the passed departmentName and courseName
        departmentName = getIntent().getStringExtra("DEPARTMENT_NAME");
        courseName = getIntent().getStringExtra("COURSE_NAME");

        courseNameTextView.setText(courseName);
        ratingsList = new ArrayList<>();
        ratingsAdapter = new AdapterForListview(this, ratingsList); // Initialize with empty list
        ratingsListView.setAdapter(ratingsAdapter);

        loadRatings();
    }

    private void loadRatings() {
        DatabaseReference ratingsRef = mDatabase.child("departments").child(departmentName).child("courses").child(courseName).child("ratings");
        ratingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ratingsList.clear();
                double totalWorkload = 0;
                double totalScore = 0;
                double totalAttendance = 0;
                double totalLateSubmission = 0;
                int count = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CourseRatingInstance rating = snapshot.getValue(CourseRatingInstance.class);
                    if (rating != null) {
                        rating.setFirebaseKey(snapshot.getKey());
                        ratingsList.add(rating);

                        // Sum up for averages
                        totalWorkload += rating.getWorkloadRating();
                        totalScore += rating.getCourseScore();
                        totalAttendance += rating.isChecksAttendance() ? 1 : 0;
                        totalLateSubmission += rating.isAllowsLateSubmission() ? 1 : 0;
                        count++;
                    }
                }

                if (count > 0) {
                    // Calculate averages
                    averageWorkloadTextView.setText("Average Workload: " + (totalWorkload / count));
                    averageRatingTextView.setText("Average Rating: " + (totalScore / count));
                    averageAttendanceTextView.setText("Average Attendance Checking: " + (totalAttendance / count));
                    averageLateSubmissionTextView.setText("Average Late Submission Allowance: " + (totalLateSubmission / count));
                }

                // Set up adapter
//                ratingsAdapter = new AdapterForListview(ViewRatingsActivity.this, ratingsList);
//                ratingsListView.setAdapter(ratingsAdapter);
                ratingsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewRatingsActivity.this, "Failed to load ratings.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}