package com.gect.connect.db;

import com.gect.connect.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * NotificationManager handles database operations for Academic Notifications and Media Sharing.
 */
public class NotificationManager {

    public static class Notification {
        public int notificationId;
        public String title;
        public String content;
        public int senderId;
        public String senderName;
        public String targetDepartment;
        public String filePath;
        public Timestamp sentAt;
    }

    /**
     * Post a new academic notification.
     */
    public static boolean postNotification(String title, String content, int senderId, String dept, String filePath) {
        String sql = "INSERT INTO notifications (title, content, sender_id, target_department, file_path) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setInt(3, senderId);
            pstmt.setString(4, dept);
            pstmt.setString(5, filePath);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    /**
     * Retrieve notifications based on department.
     */
    public static List<Notification> getNotifications(String dept) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT n.*, u.full_name FROM notifications n JOIN users u ON n.sender_id = u.user_id " +
                     "WHERE n.target_department IS NULL OR n.target_department = ? ORDER BY n.sentAt DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dept);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Notification n = new Notification();
                n.notificationId = rs.getInt("notification_id");
                n.title = rs.getString("title");
                n.content = rs.getString("content");
                n.senderId = rs.getInt("sender_id");
                n.senderName = rs.getString("full_name");
                n.targetDepartment = rs.getString("target_department");
                n.filePath = rs.getString("file_path");
                n.sentAt = rs.getTimestamp("sent_at");
                notifications.add(n);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return notifications;
    }
}
