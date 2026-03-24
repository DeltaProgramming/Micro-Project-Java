package com.gect.connect.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SettingsManager handles database operations for User Settings and Campus Events.
 */
public class SettingsManager {

    public static class CampusEvent {
        public int eventId;
        public String title;
        public String description;
        public Timestamp eventDate;
        public String location;
        public String imagePath;
    }

    /**
     * Retrieve upcoming campus events.
     */
    public static List<CampusEvent> getCampusEvents() {
        List<CampusEvent> events = new ArrayList<>();
        String sql = "SELECT * FROM campus_events WHERE event_date >= CURRENT_TIMESTAMP ORDER BY event_date ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                CampusEvent e = new CampusEvent();
                e.eventId = rs.getInt("event_id");
                e.title = rs.getString("title");
                e.description = rs.getString("description");
                e.eventDate = rs.getTimestamp("event_date");
                e.location = rs.getString("location");
                e.imagePath = rs.getString("image_path");
                events.add(e);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return events;
    }

    /**
     * Update user settings.
     */
    public static boolean updateSettings(int userId, String theme, boolean notificationsEnabled) {
        String sql = "INSERT INTO user_settings (user_id, theme, notification_enabled) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE theme = ?, notification_enabled = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, theme);
            pstmt.setBoolean(3, notificationsEnabled);
            pstmt.setString(4, theme);
            pstmt.setBoolean(5, notificationsEnabled);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
