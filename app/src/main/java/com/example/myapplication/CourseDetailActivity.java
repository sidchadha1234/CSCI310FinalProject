package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CourseDetailActivity extends AppCompatActivity {

    private TextView textViewDescription;
    private TextView textViewUnits;
    private TextView textViewTime;
    private TextView textViewLocation;
    private TextView textViewSection;
    private TextView textViewDays;
    private TextView textViewInstructor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // Initialize TextViews
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewUnits = findViewById(R.id.textViewUnits);
        textViewTime = findViewById(R.id.textViewTime);
        textViewLocation = findViewById(R.id.textViewLocation);
        textViewSection = findViewById(R.id.textViewSection);
        textViewDays = findViewById(R.id.textViewDays);
        textViewInstructor = findViewById(R.id.textViewInstructor);

        // Get the course name and department name passed from the previous activity
        String courseName = getIntent().getStringExtra("COURSE_NAME");
        String departmentName = getIntent().getStringExtra("DEPARTMENT_NAME");

        // Use the department name to find the corresponding course detail string array from strings.xml
        if (courseName != null && departmentName != null) {
            String formattedDepartmentName = departmentName.toLowerCase().replaceAll("\\s+", "_");
            int resId = getResources().getIdentifier(formattedDepartmentName + "_course_details", "array", getPackageName());
            String[] courseDetailsArray = getResources().getStringArray(resId);

            // Find the correct course details within the array
            String[] details = null;
            for (String detail : courseDetailsArray) {
                if (detail.startsWith(courseName + ",")) {
                    details = detail.split(",", -1); // Split the details string into parts
                    break;
                }
            }

            // If details are found, set the text for each TextView
            if (details != null && details.length >= 7) {
                textViewDescription.setText(details[0]);
                textViewUnits.setText(details[1]);
                textViewTime.setText(details[2]);
                textViewLocation.setText(details[3]);
                textViewSection.setText(details[4]);
                textViewDays.setText(details[5]);
                textViewInstructor.setText(details[6]);
            }
        }
    }
}
