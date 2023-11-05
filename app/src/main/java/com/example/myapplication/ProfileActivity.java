package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private EditText nameEditText, roleEditText, uscIdEditText;
    private Button saveProfileButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImageView = findViewById(R.id.profileImageView);
        nameEditText = findViewById(R.id.nameEditText);
       // roleEditText = findViewById(R.id.roleEditText); // You need to add this EditText to your layout
       // uscIdEditText = findViewById(R.id.uscIdEditText); // And this one
        saveProfileButton = findViewById(R.id.saveProfileButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        saveProfileButton.setOnClickListener(view -> saveUserProfile());
    }

    private void saveUserProfile() {
        String name = nameEditText.getText().toString().trim();
        String role = roleEditText.getText().toString().trim();
        String uscId = uscIdEditText.getText().toString().trim();

        // Simple validation
        if (name.isEmpty() || role.isEmpty() || uscId.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Assuming you have a User class to hold the profile data
        User user = new User(name, role, uscId);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Save the user information to the database
        databaseReference.child(userId).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Profile updated!", Toast.LENGTH_SHORT).show();
                        // Redirect to profile view or home activity
                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed to update profile!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public class User {
        public String name;
        public String role;
        public String uscId;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String name, String role, String uscId) {
            this.name = name;
            this.role = role;
            this.uscId = uscId;
        }

        // Getters and setters...
    }
}

