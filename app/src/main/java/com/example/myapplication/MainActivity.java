package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.widget.Spinner;
public class MainActivity extends AppCompatActivity {
    private Spinner userTypeSpinner;

    private FirebaseAuth mAuth;
    private EditText signUpNameEditText, signUpUSCIDEditText, signUpPasswordEditText, confirmPasswordEditText;
    private EditText editTextGmail, editTextUSCID, loginUSCIDEditText, loginPasswordEditText;
    private Button signUpConfirmButton, loginButton;
    private ImageView imageViewProfile;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // UI components
        signUpNameEditText = findViewById(R.id.signUpNameEditText);
        signUpUSCIDEditText = findViewById(R.id.signUpUSCIDEditText);
        signUpPasswordEditText = findViewById(R.id.signUpPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        editTextGmail = findViewById(R.id.editTextGmail);
        userTypeSpinner = findViewById(R.id.userTypeSpinner); // Initialize the Spinner for user type

        editTextUSCID = findViewById(R.id.signUpUSCIDEditText);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        signUpConfirmButton = findViewById(R.id.signUpConfirmButton);
        loginUSCIDEditText = findViewById(R.id.loginUSCIDEditText);
        loginPasswordEditText = findViewById(R.id.loginPasswordEditText);
        loginButton = findViewById(R.id.loginButton);

        imageViewProfile.setOnClickListener(v -> openGallery());

        signUpConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = signUpNameEditText.getText().toString();
                String uscID = signUpUSCIDEditText.getText().toString();
                String password = signUpPasswordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                String userType = userTypeSpinner.getSelectedItem().toString(); // Retrieve selected user type

                String gmail = editTextGmail.getText().toString();
                String uscIdInput = editTextUSCID.getText().toString();

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(MainActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!gmail.endsWith("@gmail.com") || gmail.length() < 10) {
                    Toast.makeText(MainActivity.this, "Please enter a valid Gmail address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (uscIdInput.length() != 10 || !uscIdInput.matches("\\d{10}")) {
                    Toast.makeText(MainActivity.this, "USC ID must be 10 digits.", Toast.LENGTH_SHORT).show();
                    return;
                }

                signUp(gmail, password, userType); // Include userType in signUp method call
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uscID = loginUSCIDEditText.getText().toString();
                String password = loginPasswordEditText.getText().toString();
                login(uscID, password);
            }
        });
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
        }
    }

    private void signUp(String email, String password, String userType) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Get the newly created user
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        // Create a new User object with an empty courses list
                        String name = signUpNameEditText.getText().toString();
                        String uscID = signUpUSCIDEditText.getText().toString();
                        User user = new User(name, uscID, email, userType);

                        // Save the user information and then the image
                        if (firebaseUser != null) {
                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("students");
                            usersRef.child(firebaseUser.getUid()).setValue(user)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            // Only proceed with image upload if user data save is successful
                                            uploadImageAndSaveUri(firebaseUser);
                                        } else {
                                            Toast.makeText(MainActivity.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Log.e("SIGNUP_ERROR", "Error: " + errorMessage, task.getException());
                        Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void uploadImageAndSaveUri(FirebaseUser firebaseUser) {
        if (imageUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("profileImages/" + firebaseUser.getUid() + ".jpg");
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL
                        Task<Uri> downloadUriTask = taskSnapshot.getStorage().getDownloadUrl();
                        downloadUriTask.addOnSuccessListener(uri -> {
                            // Save the download URL to the user's profile
                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("students");
                            usersRef.child(firebaseUser.getUid()).child("profileImageUrl").setValue(uri.toString())
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, "Image Upload Successful", Toast.LENGTH_SHORT).show();
                                            navigateToHomePage();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        });
                    })
                    .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            navigateToHomePage(); // No image to upload, just navigate
        }
    }
    private void navigateToHomePage() {
        // Go to home page if successful
        Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
        startActivity(intent);
        finish();
    }




    private void login(String uscID, String password) {
        mAuth.signInWithEmailAndPassword(uscID, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();


                        //go to home page if successful
                        Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "Login Failed! Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
