package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class registeredActivity extends AppCompatActivity {

    private ListView listView;
    private UserAdapter adapter;
    private List<String> registeredUserIds = new ArrayList<>();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_classes);

        listView = findViewById(R.id.lv_registered_classes);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference(); // Reference to the root of the database

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            adapter = new UserAdapter(this, registeredUserIds, currentUserId);
            listView.setAdapter(adapter);

            // Listen for single value event at the root of your database
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Iterate through all departments
                    for (DataSnapshot departmentSnapshot : dataSnapshot.getChildren()) {
                        // Skip the "students" node
                        if (departmentSnapshot.getKey().equals("students")) continue;

                        // Iterate through all courses within a department
                        for (DataSnapshot courseSnapshot : departmentSnapshot.getChildren()) {
                            String courseName = courseSnapshot.getKey();
                            DataSnapshot studentsSnapshot = courseSnapshot.child("students");

                            // Check if the current user is enrolled in the course
                            if (studentsSnapshot.hasChild(currentUserId)) {
                                // Add course to the list with department name
                                String departmentName = departmentSnapshot.getKey();
                                registeredUserIds.add("Course: " + departmentName + " - " + courseName);

                                // Iterate over student IDs
                                for (DataSnapshot studentSnapshot : studentsSnapshot.getChildren()) {
                                    String studentId = studentSnapshot.getKey();
                                    if (!studentId.equals(currentUserId)) {
                                        registeredUserIds.add(studentId);
                                    }
                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged(); // Notify the adapter of the data change
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });
        } else {
            // No user is signed in
        }
    }
}