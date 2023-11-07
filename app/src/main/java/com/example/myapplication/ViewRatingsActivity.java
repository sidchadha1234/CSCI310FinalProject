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
        //DatabaseReference ratingsRef = mDatabase.child("departments").child(departmentName).child("courses").child(courseName).child("ratings");
        DatabaseReference ratingsRef = mDatabase.child(departmentName).child(courseName).child("ratings");

        ratingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ratingsList.clear();
                double totalWorkload = 0;
                double totalScore = 0;
                int totalAttendance = 0;
                int totalLateSubmission = 0;
                int count = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CourseRatingInstance rating = snapshot.getValue(CourseRatingInstance.class);
                    if (rating != null) {
                        rating.setFirebaseKey(snapshot.getKey());
                        ratingsList.add(rating);

                        // Handle possible null values and ensure null-safe summing up for averages
                        totalWorkload += (rating.getWorkloadRating() != null) ? rating.getWorkloadRating() : 0;
                        totalScore += (rating.getCourseScore() != null) ? rating.getCourseScore() : 0;

                        // Check if checksAttendance and allowsLateSubmission are not null before unboxing
                        totalAttendance += Boolean.TRUE.equals(rating.isChecksAttendance()) ? 1 : 0;
                        totalLateSubmission += Boolean.TRUE.equals(rating.isAllowsLateSubmission()) ? 1 : 0;
                        count++;
                    }
                }

                //ratings sorted in descending order by upvotes
                //same amount of upvotes, then longer string comment on top
                ratingsList.sort((rating1, rating2) -> {
                    int upvotes1 = (rating1.getUpvotes() != null) ? rating1.getUpvotes() : 0;
                    int upvotes2 = (rating2.getUpvotes() != null) ? rating2.getUpvotes() : 0;

                    // First, sort by upvotes
                    if (upvotes1 != upvotes2) {
                        return Integer.compare(upvotes2, upvotes1); // Descending order of upvotes
                    }

                    // If upvotes are equal, use comment length as a secondary criterion
                    int commentLength1 = rating1.getComments() != null ? rating1.getComments().length() : 0;
                    int commentLength2 = rating2.getComments() != null ? rating2.getComments().length() : 0;
                    return Integer.compare(commentLength2, commentLength1); // Descending order of comment length
                });

                if (count > 0) {
                    // Calculate averages
                    averageWorkloadTextView.setText("Average Workload: " + (totalWorkload / count));
                    averageRatingTextView.setText("Average Rating: " + (totalScore / count));
                    averageAttendanceTextView.setText("Average Attendance Checking: " + (totalAttendance / (double) count));
                    averageLateSubmissionTextView.setText("Average Late Submission Allowance: " + (totalLateSubmission / (double) count));
                }

                ratingsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewRatingsActivity.this, "Failed to load ratings.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}