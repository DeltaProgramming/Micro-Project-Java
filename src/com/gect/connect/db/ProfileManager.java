package com.gect.connect.db;

import com.gect.connect.model.User;
import java.sql.*;

/**
 * ProfileManager handles database operations for User Profile Management.
 */
public class ProfileManager {

    /**
     * Update user profile details.
     */
    public static boolean updateProfile(User user) {
        String sql = "UPDATE users SET full_name = ?, bio = ?, mobile = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getBio());
            pstmt.setString(3, user.getMobile());
            pstmt.setInt(4, user.getUserId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update profile picture path.
     */
    public static boolean updateProfilePicture(int userId, String picPath) {
        String sql = "UPDATE users SET profile_pic_path = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, picPath);
            pstmt.setInt(2, userId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update user status (bio).
     */
    public static boolean updateStatus(int userId, String status) {
        String sql = "UPDATE users SET bio = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, userId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieve user statistics (e.g., message count, group count).
     */
    public static int getMessageCount(int userId) {
        String sql = "SELECT COUNT(*) FROM messages WHERE sender_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public static int getGroupCount(int userId) {
        String sql = "SELECT COUNT(*) FROM group_members WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
}
