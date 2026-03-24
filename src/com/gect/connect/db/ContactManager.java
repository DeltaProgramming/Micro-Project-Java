package com.gect.connect.db;

import com.gect.connect.model.Staff;
import com.gect.connect.model.Student;
import com.gect.connect.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ContactManager handles database operations for Contact Management.
 */
public class ContactManager {

    /**
     * Search users by name, department, or roll number.
     */
    public static List<User> searchUsers(String query) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE full_name LIKE ? OR department LIKE ? OR roll_no_emp_id LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchStr = "%" + query + "%";
            pstmt.setString(1, searchStr);
            pstmt.setString(2, searchStr);
            pstmt.setString(3, searchStr);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(extractUser(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return users;
    }

    /**
     * Send connection request.
     */
    public static boolean sendRequest(int userId, int friendId) {
        String sql = "INSERT INTO contacts (user_id, friend_id, status) VALUES (?, ?, 'pending')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, friendId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    /**
     * Accept/Reject/Block connection request.
     */
    public static boolean updateContactStatus(int userId, int friendId, String status) {
        String sql = "UPDATE contacts SET status = ? WHERE user_id = ? AND friend_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, friendId); // The person who received the request
            pstmt.setInt(3, userId); // The person who sent the request
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    /**
     * Retrieve contacts for a user.
     */
    public static List<User> getContacts(int userId) {
        List<User> contacts = new ArrayList<>();
        String sql = "SELECT u.* FROM users u JOIN contacts c ON (u.user_id = c.friend_id OR u.user_id = c.user_id) " +
                     "WHERE (c.user_id = ? OR c.friend_id = ?) AND c.status = 'accepted' AND u.user_id != ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                contacts.add(extractUser(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return contacts;
    }

    /**
     * Retrieve pending requests for a user.
     */
    public static List<User> getPendingRequests(int userId) {
        List<User> requests = new ArrayList<>();
        String sql = "SELECT u.* FROM users u JOIN contacts c ON u.user_id = c.user_id WHERE c.friend_id = ? AND c.status = 'pending'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                requests.add(extractUser(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return requests;
    }

    private static User extractUser(ResultSet rs) throws SQLException {
        String role = rs.getString("role");
        User user;
        if ("Student".equals(role)) {
            user = new Student(rs.getInt("user_id"), rs.getString("full_name"), rs.getString("email"), rs.getString("department"), rs.getString("roll_no_emp_id"));
        } else {
            user = new Staff(rs.getInt("user_id"), rs.getString("full_name"), rs.getString("email"), rs.getString("department"), rs.getString("roll_no_emp_id"));
        }
        user.setBio(rs.getString("bio"));
        user.setProfilePicPath(rs.getString("profile_pic_path"));
        return user;
    }
}
