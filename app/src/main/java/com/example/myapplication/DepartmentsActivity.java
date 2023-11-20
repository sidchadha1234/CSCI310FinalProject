package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import androidx.core.content.ContextCompat;

public class DepartmentsActivity extends AppCompatActivity {

    private Spinner spinnerDepartments;
    private ListView listViewCourses;
    public Button buttonSelectCourse;
    private TextView departmentLabel;
    private String selectedCourse; // Variable to store the selected course
    private String currentDepartment; // Variable to store the currently viewed department

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_departments);

        spinnerDepartments = findViewById(R.id.spinnerDepartments);
        listViewCourses = findViewById(R.id.listViewCourses);
        buttonSelectCourse = findViewById(R.id.buttonSelectCourse);
        departmentLabel = findViewById(R.id.departmentLabel);

        setUpSpinner();
        setUpListView();
        setUpButton();
    }

    private void setUpSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.department_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartments.setAdapter(adapter);

        spinnerDepartments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentDepartment = parent.getItemAtPosition(position).toString();
                updateCourseListView(currentDepartment);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Code to handle no selection
            }
        });
    }

    private void setUpListView() {
        listViewCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    parent.getChildAt(i).setBackgroundColor(ContextCompat.getColor(DepartmentsActivity.this, android.R.color.transparent));
                }
                view.setBackgroundColor(ContextCompat.getColor(DepartmentsActivity.this, R.color.selected_item));
                selectedCourse = (String) parent.getItemAtPosition(position);
            }
        });
    }

    private void setUpButton() {
        buttonSelectCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCourse != null && !selectedCourse.isEmpty()) {
                    Intent intent = new Intent(DepartmentsActivity.this, CourseDetailActivity.class);
                    intent.putExtra("COURSE_NAME", selectedCourse);
                    intent.putExtra("DEPARTMENT_NAME", currentDepartment);
                    startActivity(intent);
                } else {
                    Toast.makeText(DepartmentsActivity.this, "Please select a course first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateCourseListView(String department) {
        int arrayId = getResources().getIdentifier(department.replaceAll("\\s+", "_").toLowerCase() + "_courses", "array", getPackageName());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayId, android.R.layout.simple_list_item_1);
        listViewCourses.setAdapter(adapter);
    }

    // Additional methods for UI interactions and other functionalities can be added here
}
