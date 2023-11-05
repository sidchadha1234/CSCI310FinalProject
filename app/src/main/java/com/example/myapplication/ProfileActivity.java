package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class ProfileActivity extends AppCompatActivity {

    private EditText nameEditText, uscIdEditText, emailEditText;
    private Button saveButton;
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase Auth and Database
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI components
        nameEditText = findViewById(R.id.editTextName);
        uscIdEditText = findViewById(R.id.editTextUSCID);
        emailEditText = findViewById(R.id.editTextEmail);
        saveButton = findViewById(R.id.buttonSave);

        // Load user information
        loadUserInfo();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
            }
        });
    }

    private void loadUserInfo() {
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = mDatabase.child("students").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Assuming the user's name, USC ID, and email are direct children of the user's node
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String uscID = dataSnapshot.child("uscID").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);

                    // Set the EditText fields with the retrieved data
                    nameEditText.setText(name != null ? name : "");
                    uscIdEditText.setText(uscID != null ? uscID : "");
                    emailEditText.setText(email != null ? email : "");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Getting data failed, log a message
                    Log.w("ProfileActivity", "loadUserInfo:onCancelled", databaseError.toException());
                    Toast.makeText(ProfileActivity.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Handle the case where the user is not logged in
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserInfo() {
        // Here you would save the user's information to the database
        String name = nameEditText.getText().toString();
        String uscID = uscIdEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (user != null) {
            String userId = user.getUid();
            mDatabase.child("students").child(userId).child("name").setValue(name);
            mDatabase.child("students").child(userId).child("uscID").setValue(uscID);
            mDatabase.child("students").child(userId).child("email").setValue(email)
                    .addOnSuccessListener(aVoid -> Toast.makeText(ProfileActivity.this, "Profile updated!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Failed to update profile.", Toast.LENGTH_SHORT).show());
        }
    }
}
