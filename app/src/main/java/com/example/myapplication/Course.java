package com.example.myapplication;

import java.util.Map;

public class Course {
    private int enrolled;
    private int capacity;
    private Map<String, Boolean> students; // Map to hold student UIDs

    public Course() {
        // Default constructor required for calls to DataSnapshot.getValue(Course.class)
    }

    public int getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(int enrolled) {
        this.enrolled = enrolled;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Map<String, Boolean> getStudents() {
        return students;
    }

    public void setStudents(Map<String, Boolean> students) {
        this.students = students;
    }
}
