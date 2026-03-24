package com.gect.connect.db;

import com.gect.connect.model.Staff;
import com.gect.connect.model.Student;
import com.gect.connect.model.User;
import java.sql.*;

/**
 * AuthManager handles database operations for User Registration and Authentication.
 */
public class AuthManager {

    /**
     * Register a new user in the database.
     */
    public static boolean registerUser(User user) {
        String sql = "INSERT INTO users (full_name, email, department, role, roll_no_emp_id, mobile, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getDepartment());
            pstmt.setString(4, user.getRole());
            
            String id = (user instanceof Student) ? ((Student)user).getRollNo() : ((Staff)user).getEmployeeId();
            pstmt.setString(5, id);
            pstmt.setString(6, user.getMobile() != null ? user.getMobile() : "");
            pstmt.setString(7, user.getPassword());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validate user login.
     */
    public static User login(String email, String password) {
        // Log attempt for debugging
        System.out.println("Login attempt for: " + email);
        
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("User found in database!");
                String role = rs.getString("role");
                User user;
                if ("Student".equals(role)) {
                    user = new Student(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("department"),
                        rs.getString("roll_no_emp_id")
                    );
                } else {
                    user = new Staff(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("department"),
                        rs.getString("roll_no_emp_id")
                    );
                }
                user.setMobile(rs.getString("mobile"));
                user.setBio(rs.getString("bio"));
                user.setProfilePicPath(rs.getString("profile_pic_path"));
                user.setJoinDate(rs.getTimestamp("join_date"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Check if email is already registered.
     */
    public static boolean isEmailRegistered(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email.toLowerCase().trim());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update password (for forgot password functionality).
     */
    public static boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, email.toLowerCase().trim());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
