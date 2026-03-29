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

    public static class UserSettings {
        public int userId;
        public String theme = "Light";
        public boolean notificationEnabled = true;
        public String notificationSound = "Default";
        public String privacyLastSeen = "Everyone";
        public String privacyProfilePhoto = "Everyone";
    }

    /**
     * Retrieve user settings.
     */
    public static UserSettings getSettings(int userId) {
        UserSettings s = new UserSettings();
        s.userId = userId;
        String sql = "SELECT * FROM user_settings WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                s.theme = rs.getString("theme");
                s.notificationEnabled = rs.getBoolean("notification_enabled");
                s.notificationSound = rs.getString("notification_sound");
                s.privacyLastSeen = rs.getString("privacy_last_seen");
                s.privacyProfilePhoto = rs.getString("privacy_profile_photo");
            } else {
                // Initialize default settings if not exists
                updateSettings(s);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return s;
    }

    /**
     * Update user settings using a UserSettings object.
     */
    public static boolean updateSettings(UserSettings s) {
        String sql = "INSERT INTO user_settings (user_id, theme, notification_enabled, notification_sound, privacy_last_seen, privacy_profile_photo) " +
                     "VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
                     "theme = ?, notification_enabled = ?, notification_sound = ?, privacy_last_seen = ?, privacy_profile_photo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, s.userId);
            pstmt.setString(2, s.theme);
            pstmt.setBoolean(3, s.notificationEnabled);
            pstmt.setString(4, s.notificationSound);
            pstmt.setString(5, s.privacyLastSeen);
            pstmt.setString(6, s.privacyProfilePhoto);
            pstmt.setString(7, s.theme);
            pstmt.setBoolean(8, s.notificationEnabled);
            pstmt.setString(9, s.notificationSound);
            pstmt.setString(10, s.privacyLastSeen);
            pstmt.setString(11, s.privacyProfilePhoto);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    /**
     * Update basic user settings.
     * @deprecated Use updateSettings(UserSettings s) instead.
     */
    @Deprecated
    public static boolean updateSettings(int userId, String theme, boolean notificationsEnabled) {
        UserSettings s = getSettings(userId);
        s.theme = theme;
        s.notificationEnabled = notificationsEnabled;
        return updateSettings(s);
    }
}
