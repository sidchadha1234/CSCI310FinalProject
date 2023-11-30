package com.example.myapplication;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterForListview extends ArrayAdapter<CourseRatingInstance> {
    private int resourceLayout;
    private Context mContext;
    private DatabaseReference mDatabase;

    public AdapterForListview(Context context, List<CourseRatingInstance> items) {
        super(context, R.layout.individual_ratings_within_list_format, items); // Update this layout reference if needed
        this.resourceLayout = R.layout.individual_ratings_within_list_format; // Update this layout reference if needed
        this.mContext = context;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        CourseRatingInstance p = getItem(position);
        String currUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (p != null) {
            TextView tvUsername = v.findViewById(R.id.usernameTextView);
            TextView tvWorkload = v.findViewById(R.id.tvWorkloadRating);
            TextView tvRating = v.findViewById(R.id.tvClassRating);
            TextView tvComments = v.findViewById(R.id.tvComments);
            TextView tvAttendanceCheck = v.findViewById(R.id.tvAttendanceCheck);
            TextView tvLateHW = v.findViewById(R.id.tvLateHW);
            Button btnUpvote = v.findViewById(R.id.btnUpvote);
            Button btnDownvote = v.findViewById(R.id.btnDownvote);
            Button btnEdit = v.findViewById(R.id.btnEdit); // Make sure you have a button with this ID in your layout

            Log.d("DebugInfo", "currUserID: '" + currUserID + "'");
            Log.d("DebugInfo", "p.getUserId(): '" + p.getUserId() + "'");
            if(currUserID.equals(p.getUserId())) {
                btnEdit.setVisibility(View.VISIBLE);
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, EditRating.class);
                        intent.putExtra("DEPARTMENT_NAME", p.getDepartmentName()); // Pass department name
                        intent.putExtra("COURSE_NAME", p.getCourseName()); // Pass course name
                        intent.putExtra("RATING_KEY", p.getFirebaseKey()); // Pass the key of the rating to be edited
                        mContext.startActivity(intent);
                    }
                });
            } else {
                btnEdit.setVisibility(View.GONE); // Or View.INVISIBLE if you want to keep its space
            }

            //CourseRatingInstance p = getItem(position); update p

            tvUsername.setText("Username: " + p.getUsername());
            tvWorkload.setText("Workload Rating: " + p.getWorkloadRating());
            tvRating.setText("Course Rating: " + p.getCourseScore());
            tvComments.setText("Comments: " + p.getComments());
            tvAttendanceCheck.setText(p.isChecksAttendance() ? "Prof Checks Attendance: Yes" : "Prof Checks Attendance: No");
            tvLateHW.setText(p.isAllowsLateSubmission() ? "Prof Allows Late HW: Yes" : "Prof Allows Late HW: No");

            int initialUpvotes = p.getUpvotes() != null ? p.getUpvotes() : 0;
            int initialDownvotes = p.getDownvotes() != null ? p.getDownvotes() : 0;
            btnUpvote.setText("Upvote " + initialUpvotes);
            btnDownvote.setText("Downvote " + initialDownvotes);

            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference interactionsRef = mDatabase.child(p.getDepartmentName()).child(p.getCourseName()).child("ratings").child(p.getFirebaseKey()).child("interactions");

            interactionsRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String interactionType = dataSnapshot.getValue(String.class);
                        if ("like".equals(interactionType)) {
                            btnUpvote.setEnabled(false);
                            btnDownvote.setEnabled(true);
                        } else if ("dislike".equals(interactionType)) {
                            btnDownvote.setEnabled(false);
                            btnUpvote.setEnabled(true);
                        }
                    } else {
                        btnUpvote.setEnabled(true);
                        btnDownvote.setEnabled(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(mContext, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            btnUpvote.setOnClickListener(y -> updateVote(interactionsRef, currentUserId, "like", p, btnUpvote, btnDownvote, true));
            btnDownvote.setOnClickListener(y -> updateVote(interactionsRef, currentUserId, "dislike", p, btnDownvote, btnUpvote, false));
        }

        return v;
    }

    private void updateVote(DatabaseReference interactionsRef, String userId, String voteType, CourseRatingInstance rating, Button votedButton, Button otherButton, boolean isUpvote) {
        interactionsRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String previousVote = dataSnapshot.getValue(String.class);
                boolean isChangingVote = previousVote != null && !previousVote.equals(voteType);

                // Set the new vote type
                interactionsRef.child(userId).setValue(voteType).addOnSuccessListener(aVoid -> {
                    DatabaseReference ratingRef = mDatabase.child(rating.getDepartmentName()).child(rating.getCourseName()).child("ratings").child(rating.getFirebaseKey());

                    ratingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int currentUpvotes = dataSnapshot.child("upvotes").getValue(Integer.class);
                            int currentDownvotes = dataSnapshot.child("downvotes").getValue(Integer.class);

                            // Adjust vote counts based on previous vote and new vote
                            if (isUpvote) {
                                if (!isChangingVote || "dislike".equals(previousVote)) {
                                    rating.setUpvotes(currentUpvotes + 1);
                                }
                                if ("dislike".equals(previousVote) && currentDownvotes > 0) {
                                    rating.setDownvotes(currentDownvotes - 1);
                                }
                            } else {
                                if (!isChangingVote || "like".equals(previousVote)) {
                                    rating.setDownvotes(currentDownvotes + 1);
                                }
                                if ("like".equals(previousVote) && currentUpvotes > 0) {
                                    rating.setUpvotes(currentUpvotes - 1);
                                }
                            }

                            // Push the updated counts back to the database
                            ratingRef.child("upvotes").setValue(rating.getUpvotes());
                            ratingRef.child("downvotes").setValue(rating.getDownvotes());

                            // Update the button texts
                            votedButton.setText((isUpvote ? "Upvote " : "Downvote ") + (isUpvote ? rating.getUpvotes() : rating.getDownvotes()));
                            otherButton.setText((isUpvote ? "Downvote " : "Upvote ") + (isUpvote ? rating.getDownvotes() : rating.getUpvotes()));
                            votedButton.setEnabled(false);
                            otherButton.setEnabled(true);

                            notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(mContext, "Failed to update counts: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }).addOnFailureListener(e -> Toast.makeText(mContext, "Failed to update vote.", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, "Failed to retrieve previous vote: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

