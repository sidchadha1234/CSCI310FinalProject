package com.example.myapplication;

public class CourseRatingInstance {
    private int workloadRating;
    private int courseScore;
    private boolean checksAttendance;
    private boolean allowsLateSubmission;
    private String comments;
    private String username;
    private int upvotes;
    private int downvotes;
    private String firebaseKey;
    private String departmentName;
    private String courseName;

    // Constructor
    public CourseRatingInstance(int workloadRating, int courseScore, boolean checksAttendance, boolean allowsLateSubmission, String comments, String username, String departmentName, String courseName) {
        this.workloadRating = workloadRating;
        this.courseScore = courseScore;
        this.checksAttendance = checksAttendance;
        this.allowsLateSubmission = allowsLateSubmission;
        this.comments = comments;
        this.username = username;
        this.upvotes = 0;
        this.downvotes = 0;
        this.firebaseKey = "";
        this.departmentName = departmentName;
        this.courseName = courseName;
    }

    // Getters and Setters
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }
    public int getWorkloadRating() {
        return workloadRating;
    }

    public void setWorkloadRating(int workloadRating) {
        this.workloadRating = workloadRating;
    }

    public int getCourseScore() {
        return courseScore;
    }

    public void setCourseScore(int courseScore) {
        this.courseScore = courseScore;
    }

    public boolean isChecksAttendance() {
        return checksAttendance;
    }

    public void setChecksAttendance(boolean checksAttendance) {
        this.checksAttendance = checksAttendance;
    }

    public boolean isAllowsLateSubmission() {
        return allowsLateSubmission;
    }

    public void setAllowsLateSubmission(boolean allowsLateSubmission) {
        this.allowsLateSubmission = allowsLateSubmission;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}