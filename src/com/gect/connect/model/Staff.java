package com.gect.connect.model;

/**
 * Staff class inheriting from User.
 * Demonstrates: Inheritance, Method Overriding
 */
public class Staff extends User {
    private String employeeId;

    public Staff() {
        super();
        setRole("Staff");
    }

    public Staff(int userId, String fullName, String email, String department, String employeeId) {
        super(userId, fullName, email, department, "Staff");
        this.employeeId = employeeId;
    }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    @Override
    public String getIdentifier() {
        return "Employee ID: " + employeeId;
    }
}
