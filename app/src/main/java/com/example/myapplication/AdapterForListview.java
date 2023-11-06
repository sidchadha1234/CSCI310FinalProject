package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterForListview extends ArrayAdapter<CourseRatingInstance> {
    private int resourceLayout;
    private Context mContext;
    private DatabaseReference mDatabase;

    public AdapterForListview(Context context, List<CourseRatingInstance> items) {
        super(context, R.layout.individual_ratings_within_list_format, items); // replace R.layout.individual_rating_layout with your actual layout resource ID for individual ratings
        this.resourceLayout = R.layout.individual_ratings_within_list_format;
        this.mContext = context;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        CourseRatingInstance p = getItem(position);

        if (p != null) {
            TextView tvUsername = (TextView) v.findViewById(R.id.tvUsername);
            TextView tvRating = (TextView) v.findViewById(R.id.tvRating);
            TextView tvUpvotes = (TextView) v.findViewById(R.id.tvUpvotes);
            TextView tvDownvotes = (TextView) v.findViewById(R.id.tvDownvotes);
            Button btnUpvote = (Button) v.findViewById(R.id.btnUpvote);
            Button btnDownvote = (Button) v.findViewById(R.id.btnDownvote);

            if (tvUsername != null) {
                tvUsername.setText(p.getUsername());
            }
            if (tvRating != null) {
                String ratingText = "Workload: " + p.getWorkloadRating() + ", Score: " + p.getCourseScore();
                tvRating.setText(ratingText);
            }
            if (tvUpvotes != null) {
                tvUpvotes.setText(String.valueOf(p.getUpvotes()));
            }
            if (tvDownvotes != null) {
                tvDownvotes.setText(String.valueOf(p.getDownvotes()));
            }

            btnUpvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newUpvoteCount = p.getUpvotes() + 1;
                    mDatabase.child("departments").child(p.getDepartmentName()).child("courses").child(p.getCourseName()).child("ratings").child(p.getFirebaseKey()).child("upvotes").setValue(newUpvoteCount)
                            .addOnSuccessListener(aVoid -> {
                                p.setUpvotes(newUpvoteCount);
                                notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> Toast.makeText(mContext, "Failed to update upvote.", Toast.LENGTH_SHORT).show());
                }
            });

            btnDownvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newDownvoteCount = p.getDownvotes() + 1;
                    mDatabase.child("departments").child(p.getDepartmentName()).child("courses").child(p.getCourseName()).child("ratings").child(p.getFirebaseKey()).child("downvotes").setValue(newDownvoteCount)
                            .addOnSuccessListener(aVoid -> {
                                p.setDownvotes(newDownvoteCount);
                                notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> Toast.makeText(mContext, "Failed to update downvote.", Toast.LENGTH_SHORT).show());
                }
            });
        }

        return v;
    }
}