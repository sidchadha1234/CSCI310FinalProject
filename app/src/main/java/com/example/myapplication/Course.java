package com.example.myapplication;

public class Course {
    private int enrolled;
    private int capacity;

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
}
