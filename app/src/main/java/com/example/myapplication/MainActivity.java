package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText signUpNameEditText, signUpUSCIDEditText, signUpPasswordEditText, confirmPasswordEditText;
    private EditText loginUSCIDEditText, loginPasswordEditText;
    private Button signUpConfirmButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Sign Up UI
        signUpNameEditText = findViewById(R.id.signUpNameEditText);
        signUpUSCIDEditText = findViewById(R.id.signUpUSCIDEditText);
        signUpPasswordEditText = findViewById(R.id.signUpPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        signUpConfirmButton = findViewById(R.id.signUpConfirmButton);

        // Login UI
        loginUSCIDEditText = findViewById(R.id.loginUSCIDEditText);
        loginPasswordEditText = findViewById(R.id.loginPasswordEditText);
        loginButton = findViewById(R.id.loginButton);

        // Sign Up logic
        signUpConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = signUpNameEditText.getText().toString();
                String uscID = signUpUSCIDEditText.getText().toString();
                String password = signUpPasswordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if (password.equals(confirmPassword)) {
                    signUp(uscID, password);
                } else {
                    Toast.makeText(MainActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Login logic
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uscID = loginUSCIDEditText.getText().toString();
                String password = loginPasswordEditText.getText().toString();
                login(uscID, password);
            }
        });
    }

    private void signUp(String uscID, String password) {
        mAuth.createUserWithEmailAndPassword(uscID, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(MainActivity.this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();
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
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "Login Failed! Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
