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
    private UserAdapter adapter; // Use UserAdapter instead of ArrayAdapter
    private List<String> registeredUserIds = new ArrayList<>(); // This should be the list of user IDs
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_classes);

        listView = findViewById(R.id.lv_registered_classes);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference csRef = database.getReference("Computer Science");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            adapter = new UserAdapter(this, registeredUserIds, currentUserId); // Initialize UserAdapter
            listView.setAdapter(adapter);

            csRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                        String courseName = courseSnapshot.getKey();
                        DataSnapshot studentsSnapshot = courseSnapshot.child("students");
                        if (studentsSnapshot.hasChild(currentUserId)) { // The current user is enrolled in the course
                            // Add course to the list (if you still want to show courses)
                            registeredUserIds.add("Course: " + courseName);
                            // Iterate over student IDs
                            for (DataSnapshot studentSnapshot : studentsSnapshot.getChildren()) {
                                String studentId = studentSnapshot.getKey();
                                if (!studentId.equals(currentUserId)) { // Don't add the current user
                                    registeredUserIds.add(studentId);
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
