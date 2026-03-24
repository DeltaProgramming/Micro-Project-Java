package com.gect.connect.db;

import com.gect.connect.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ChatManager handles database operations for Individual Chat.
 */
public class ChatManager {

    public static class Message {
        public int messageId;
        public int senderId;
        public int receiverId;
        public String content;
        public String type;
        public String status;
        public Timestamp sentAt;
    }

    /**
     * Send a message to another user.
     */
    public static boolean sendMessage(int senderId, int receiverId, String content, String type) {
        String sql = "INSERT INTO messages (sender_id, receiver_id, content, message_type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, senderId);
            pstmt.setInt(2, receiverId);
            pstmt.setString(3, content);
            pstmt.setString(4, type);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    /**
     * Retrieve chat history between two users.
     */
    public static List<Message> getChatHistory(int user1, int user2) {
        List<Message> history = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY sent_at ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user1);
            pstmt.setInt(2, user2);
            pstmt.setInt(3, user2);
            pstmt.setInt(4, user1);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Message msg = new Message();
                msg.messageId = rs.getInt("message_id");
                msg.senderId = rs.getInt("sender_id");
                msg.receiverId = rs.getInt("receiver_id");
                msg.content = rs.getString("content");
                msg.type = rs.getString("message_type");
                msg.status = rs.getString("status");
                msg.sentAt = rs.getTimestamp("sent_at");
                history.add(msg);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return history;
    }

    /**
     * Update message status (e.g., mark as read).
     */
    public static void markAsRead(int senderId, int receiverId) {
        String sql = "UPDATE messages SET status = 'read' WHERE sender_id = ? AND receiver_id = ? AND status != 'read'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, senderId);
            pstmt.setInt(2, receiverId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
