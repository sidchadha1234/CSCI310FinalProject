package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DepartmentsActivity extends AppCompatActivity {

    private Spinner spinnerDepartments;
    private ListView listViewCourses;
    private Button buttonSelectCourse;
    private TextView departmentLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content of the activity to use the activity_departments.xml layout file
        setContentView(R.layout.activitiy_departments);

        // Initialize your views here
        spinnerDepartments = findViewById(R.id.spinnerDepartments);
        listViewCourses = findViewById(R.id.listViewCourses);
        buttonSelectCourse = findViewById(R.id.buttonSelectCourse);
        departmentLabel = findViewById(R.id.departmentLabel);

        // You can now set up your Spinner, ListView and other UI components here
        setUpSpinner();
    }

    private void setUpSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.department_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerDepartments.setAdapter(adapter);

        spinnerDepartments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDepartment = parent.getItemAtPosition(position).toString();
                updateCourseListView(selectedDepartment);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Code to handle no selection
            }
        });


    }
    private void updateCourseListView(String department) {
        int arrayId = getResources().getIdentifier(department.replaceAll("\\s+", "_").toLowerCase() + "_courses", "array", getPackageName());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayId, android.R.layout.simple_list_item_1);
        listViewCourses.setAdapter(adapter);
    }

    // Add more methods to handle UI interactions and other activities
}
