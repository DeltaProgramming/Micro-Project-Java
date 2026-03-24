package com.gect.connect.model;

import java.util.Date;

/**
 * Base class for all users in the GECT Connect application.
 * Demonstrates: Encapsulation (private fields, public getters/setters)
 */
public abstract class User {
    private int userId;
    private String fullName;
    private String email;
    private String department;
    private String role;
    private String rollNoEmpId;
    private String mobile;
    private String password;
    private String bio;
    private String profilePicPath;
    private boolean isOnline;
    private Date lastSeen;
    private Date joinDate;

    // Default constructor
    public User() {}

    // Constructor with parameters
    public User(int userId, String fullName, String email, String department, String role) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.department = department;
        this.role = role;
    }

    // Getters and Setters (Encapsulation)
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getRollNoEmpId() { return rollNoEmpId; }
    public void setRollNoEmpId(String rollNoEmpId) { this.rollNoEmpId = rollNoEmpId; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getProfilePicPath() { return profilePicPath; }
    public void setProfilePicPath(String profilePicPath) { this.profilePicPath = profilePicPath; }

    public boolean isOnline() { return isOnline; }
    public void setOnline(boolean online) { isOnline = online; }

    public Date getLastSeen() { return lastSeen; }
    public void setLastSeen(Date lastSeen) { this.lastSeen = lastSeen; }

    public Date getJoinDate() { return joinDate; }
    public void setJoinDate(Date joinDate) { this.joinDate = joinDate; }

    // Abstract method to demonstrate abstraction
    public abstract String getIdentifier();
}
