package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewProfileActivity extends AppCompatActivity {

    private TextView nameTextView, uscIdTextView, emailTextView;
    private DatabaseReference mDatabase;
    private ImageView imageViewProfile;
    private String userId;
    private Button buttonReturnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI components
        nameTextView = findViewById(R.id.textViewName);
        uscIdTextView = findViewById(R.id.textViewUSCID);
        emailTextView = findViewById(R.id.textViewEmail);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        buttonReturnHome = findViewById(R.id.buttonReturnHome);

        // Get the user ID from the intent
        userId = getIntent().getStringExtra("USER_ID");

        // Load user information and profile image
        loadUserInfo();
        loadProfileImage();
        buttonReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // This will close the current activity and return to HomeActivity
            }
        });
    }

    private void loadUserInfo() {
        if (userId != null) {
            DatabaseReference userRef = mDatabase.child("students").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String uscID = dataSnapshot.child("uscID").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);

                    nameTextView.setText(name != null ? name : "");
                    uscIdTextView.setText(uscID != null ? uscID : "");
                    emailTextView.setText(email != null ? email : "");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("ViewProfileActivity", "loadUserInfo:onCancelled", databaseError.toException());
                }
            });
        }
    }

    private void loadProfileImage() {
        if (userId != null) {
            DatabaseReference userRef = mDatabase.child("students").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String imageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(ViewProfileActivity.this)
                                .load(imageUrl)
                                .into(imageViewProfile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }
}
