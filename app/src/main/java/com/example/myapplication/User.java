package com.example.myapplication;
import java.util.ArrayList;

public class User {
    public String name;
    public String uscID;
    public String email;
    public String userType;
    public ArrayList<String> courses; // List to hold user courses

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String uscID, String email, String userType) {
        this.name = name;
        this.uscID = uscID;
        this.email = email;
        this.userType = userType;
        this.courses = new ArrayList<>(); // Initialize the courses list as empty
    }
}
