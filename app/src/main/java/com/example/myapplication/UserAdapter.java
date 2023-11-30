package com.example.myapplication;

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

public class UserAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> userIds;
    private String currentUserId;
    private DatabaseReference mDatabase;

    public UserAdapter(Context context, List<String> userIds, String currentUserId) {
        super(context, 0, userIds);
        this.context = context;
        this.userIds = userIds;
        this.currentUserId = currentUserId;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    public void setDatabase(DatabaseReference database) {
        this.mDatabase = database;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View finalConvertView; // Declare a final reference to the convertView
        if (convertView == null) {
            finalConvertView = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
        } else {
            finalConvertView = convertView;
        }

        TextView textViewUser = finalConvertView.findViewById(R.id.textViewUser);
        Button buttonMessage = finalConvertView.findViewById(R.id.buttonMessage);

        String userId = getItem(position);

        // Cancel any previous listener before setting a new one
        final ValueEventListener previousListener = (ValueEventListener) finalConvertView.getTag(R.id.tag_database_listener);
        if (previousListener != null) {
            mDatabase.child("students").child((String)finalConvertView.getTag(R.id.tag_user_id)).removeEventListener(previousListener);
        }
        Button buttonBlock = finalConvertView.findViewById(R.id.buttonBlock);

        // Handle block button click
        buttonBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockUser(userId);
            }
        });
        // Set the tag for the userId to the finalConvertView
        finalConvertView.setTag(R.id.tag_user_id, userId);

        // Check if the userId starts with "Course:" and set the text view accordingly
        if (userId.startsWith("Course:")) {
            textViewUser.setText(userId.substring(7)); // Remove "Course:" prefix when displaying
            buttonMessage.setVisibility(View.GONE);
            buttonBlock.setVisibility(View.GONE);

        } else if (userId.equals(currentUserId)) {
            textViewUser.setText("You");
            buttonMessage.setVisibility(View.GONE);
        } else {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Make sure the view hasn't been recycled by another item
                    if (finalConvertView.getTag(R.id.tag_user_id).equals(userId)) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        textViewUser.setText(name != null ? name : "Unknown User");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("UserAdapter", "loadUserInfo:onCancelled", databaseError.toException());
                    // Only update if the view hasn't been recycled
                    if (finalConvertView.getTag(R.id.tag_user_id).equals(userId)) {
                        textViewUser.setText("Error Loading Name");
                    }
                }
            };

            // Store the listener in the view's tag so it can be cancelled if the view is recycled
            finalConvertView.setTag(R.id.tag_database_listener, valueEventListener);

            buttonMessage.setVisibility(View.VISIBLE);
            buttonMessage.setOnClickListener(v -> {
                Intent intent = new Intent(context, MessagingActivity.class);
                intent.putExtra("OTHER_USER_ID", userId);
                context.startActivity(intent);
            });

            mDatabase.child("students").child(userId).addListenerForSingleValueEvent(valueEventListener);
        }

        return finalConvertView;
    }

    private void blockUser(String userIdToBlock) {
        // Logic to block the user
        String myUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("blocks").child(myUserId).child(userIdToBlock).setValue(true);
    }



}
