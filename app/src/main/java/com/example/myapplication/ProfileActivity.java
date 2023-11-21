package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class ProfileActivity extends AppCompatActivity {

    public EditText nameEditText, uscIdEditText, emailEditText;
    private Button saveButton;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private ImageView imageViewProfile;
    private Button buttonChangeImage;
    private static final int PICK_IMAGE = 100;
    private Uri imageUri;

    private Button buttonReturnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase Auth and Database
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI components
        buttonReturnHome = findViewById(R.id.buttonReturnHome);
        nameEditText = findViewById(R.id.editTextName);
        uscIdEditText = findViewById(R.id.editTextUSCID);
        emailEditText = findViewById(R.id.editTextEmail);
        saveButton = findViewById(R.id.buttonSave);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        buttonChangeImage = findViewById(R.id.buttonChangeImage);

        // Load user information
        loadUserInfo();
        loadProfileImage();

        buttonChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
            }
        });

        buttonReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // This will close the current activity and return to HomeActivity
            }
        });
    }

    public void loadUserInfo() {
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

    private void loadProfileImage() {
        if (user != null) {
            DatabaseReference userRef = mDatabase.child("students").child(user.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String imageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(ProfileActivity.this)
                                .load(imageUrl)
                                .into(imageViewProfile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ProfileActivity.this, "Failed to load profile image.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imageViewProfile.setImageURI(imageUri);
            uploadImageToFirebase();
        }
    }

    public void uploadImageToFirebase() {
        if (imageUri != null && user != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("profileImages/" + user.getUid() + ".jpg");
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL
                        Task<Uri> downloadUriTask = taskSnapshot.getStorage().getDownloadUrl();
                        downloadUriTask.addOnSuccessListener(uri -> {
                            // Save the download URL to the user's profile
                            DatabaseReference userRef = mDatabase.child("students").child(user.getUid());
                            userRef.child("profileImageUrl").setValue(uri.toString())
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ProfileActivity.this, "Image updated successfully!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ProfileActivity.this, "Failed to update image.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        });
                    })
                    .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }



    public void saveUserInfo() {
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
