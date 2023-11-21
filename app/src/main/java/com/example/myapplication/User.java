package com.example.myapplication;
import java.util.ArrayList;

public class User {
    public String name;
    public String uscID;
    public String email;
    public String userType;
    public ArrayList<String> courses; // List to hold user courses
    public String profileImageUrl; // Field to store the URL of the profile image


    public User() {
        // Default constructor required
    }

    public User(String name, String uscID, String email, String userType) {
        this.name = name;
        this.uscID = uscID;
        this.email = email;
        this.userType = userType;
        this.courses = new ArrayList<>(); // Initialize the courses list as empty
        this.profileImageUrl = ""; // Initialize the profile image URL as empty
    }

    // Getters and setters for the profile image URL
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

}
