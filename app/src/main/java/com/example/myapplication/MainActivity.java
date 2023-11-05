package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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

    private void signUp(String uscID, String password, String userType) {
        mAuth.createUserWithEmailAndPassword(uscID, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        Toast.makeText(MainActivity.this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();


                        //go to home page if successful
                        Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Log.e("SIGNUP_ERROR", "Error: " + errorMessage, task.getException());
                        Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    }

                });
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
