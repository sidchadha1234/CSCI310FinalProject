
package com.example.myapplication;

public class CourseRatingInstance {
    // Use Integer for numeric fields to handle potential null values from Firebase
    private Integer workloadRating;
    private Integer courseScore;
    private Boolean checksAttendance;
    private Boolean allowsLateSubmission;
    private String comments;
    private String username;
    private Integer upvotes;
    private Integer downvotes;
    // FirebaseKey, departmentName, and courseName are optional and can be null.
    private String firebaseKey;
    private String departmentName;
    private String courseName;

    private String userId;

    // Default no-argument constructor required for Firebase
    public CourseRatingInstance() {
        // Default constructor required for calls to DataSnapshot.getValue(CourseRatingInstance.class)
    }

    // Parametrized constructor for manual object creation
    public CourseRatingInstance(Integer workloadRating, Integer courseScore, Boolean checksAttendance, Boolean allowsLateSubmission, String comments, String username, String departmentName, String courseName, String userId) {
        this.workloadRating = workloadRating;
        this.courseScore = courseScore;
        this.checksAttendance = checksAttendance;
        this.allowsLateSubmission = allowsLateSubmission;
        this.comments = comments;
        this.username = username;
        // Set default values for upvotes and downvotes as they are integers and cannot be null.
        this.upvotes = 0;
        this.downvotes = 0;
        this.firebaseKey = "";
        this.departmentName = departmentName;
        this.courseName = courseName;
        this.userId = userId;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Integer getWorkloadRating() {
        return workloadRating != null ? workloadRating : 0; // Replace 0 with a default value if needed
    }

    public void setWorkloadRating(Integer workloadRating) {
        this.workloadRating = workloadRating;
    }


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
    public Integer getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public Integer getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }
//    public int getWorkloadRating() {
//        return workloadRating;
//    }
//
//    public void setWorkloadRating(int workloadRating) {
//        this.workloadRating = workloadRating;
//    }

    public Integer getCourseScore() {
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

