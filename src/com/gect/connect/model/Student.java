package com.gect.connect.model;

/**
 * Student class inheriting from User.
 * Demonstrates: Inheritance, Method Overriding
 */
public class Student extends User {
    private String rollNo;

    public Student() {
        super();
        setRole("Student");
    }

    public Student(int userId, String fullName, String email, String department, String rollNo) {
        super(userId, fullName, email, department, "Student");
        this.rollNo = rollNo;
    }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    @Override
    public String getIdentifier() {
        return "Student Roll No: " + rollNo;
    }
}
