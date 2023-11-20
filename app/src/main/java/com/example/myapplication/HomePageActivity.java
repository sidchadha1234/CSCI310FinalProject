package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomePageActivity extends AppCompatActivity {

    public Button profileButton, departmentsButton, messagesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        // Initialize buttons
        profileButton = findViewById(R.id.buttonProfile);
        departmentsButton = findViewById(R.id.buttonDepartments);
        messagesButton = findViewById(R.id.buttonMessages);

        // Set onClickListeners for buttons to navigate to the respective activities
        profileButton.setOnClickListener(view -> navigateToProfile());
        departmentsButton.setOnClickListener(view -> navigateToDepartments());
        messagesButton.setOnClickListener(view -> navigateToMessages());
    }


    //go to profile activity
    private void navigateToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }


    //go to departments activity
    private void navigateToDepartments() {
        Intent intent = new Intent(this, DepartmentsActivity.class);
        startActivity(intent);
    }

    //go to messages activity
    private void navigateToMessages() {
        Intent intent = new Intent(this, registeredActivity.class);
        startActivity(intent);
    }
}