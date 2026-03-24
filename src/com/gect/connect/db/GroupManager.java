package com.gect.connect.db;

import com.gect.connect.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GroupManager class for handling database operations related to groups.
 * Demonstrates: JDBC PreparedStatements, ResultSet handling.
 */
public class GroupManager {
    private Connection conn;

    public GroupManager() {
        this.conn = DBConnection.getConnection();
    }

    // 1. Create a new group (INSERT operation)
    private int createGroupInternal(String name, String description, int createdByUserId, String groupType) {
        String sql = "INSERT INTO chat_groups (group_name, description, created_by, group_type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setInt(3, createdByUserId);
            pstmt.setString(4, groupType);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int groupId = generatedKeys.getInt(1);
                        // Automatically add the creator as an Admin member
                        addMemberToGroup(groupId, createdByUserId, "Admin");
                        return groupId;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 2. Add a member to a group (INSERT operation)
    public boolean addMemberToGroup(int groupId, int userId, String role) {
        String sql = "INSERT INTO group_members (group_id, user_id, role) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, groupId);
            pstmt.setInt(2, userId);
            pstmt.setString(3, role);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Get all groups for a user (SELECT operation)
    public List<ChatGroup> getGroupsForUser(int userId) {
        List<ChatGroup> groups = new ArrayList<>();
        String sql = "SELECT cg.* FROM chat_groups cg " +
                     "JOIN group_members gm ON cg.group_id = gm.group_id " +
                     "WHERE gm.user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ChatGroup group = new ChatGroup(
                        rs.getInt("group_id"),
                        rs.getString("group_name"),
                        rs.getString("description"),
                        rs.getString("group_type")
                    );
                    groups.add(group);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    public boolean createGroup(String name, String desc, int userId, String type) {
        return createGroupInternal(name, desc, userId, type) > 0;
    }

    public boolean addParticipant(int groupId, int userId, String role) {
        return addMemberToGroup(groupId, userId, role);
    }

    public boolean removeParticipant(int groupId, int userId) {
        String sql = "DELETE FROM group_members WHERE group_id = ? AND user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, groupId);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getGroupMembers(int groupId) {
        List<User> members = new ArrayList<>();
        String sql = "SELECT u.* FROM users u JOIN group_members gm ON u.user_id = gm.user_id WHERE gm.group_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, groupId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String role = rs.getString("role");
                User user;
                if ("Student".equals(role)) {
                    user = new Student(rs.getInt("user_id"), rs.getString("full_name"), rs.getString("email"), rs.getString("department"), rs.getString("roll_no_emp_id"));
                } else {
                    user = new Staff(rs.getInt("user_id"), rs.getString("full_name"), rs.getString("email"), rs.getString("department"), rs.getString("roll_no_emp_id"));
                }
                members.add(user);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return members;
    }

    // 4. Send a group message (INSERT operation)
    public boolean sendGroupMessage(int groupId, int senderId, String content) {
        String sql = "INSERT INTO group_messages (group_id, sender_id, message_content) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, groupId);
            pstmt.setInt(2, senderId);
            pstmt.setString(3, content);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Get group messages (SELECT operation)
    public List<GroupMessage> getGroupMessages(int groupId) {
        List<GroupMessage> messages = new ArrayList<>();
        String sql = "SELECT gm.*, u.full_name, u.email, u.department, u.role, u.roll_no_emp_id " +
                     "FROM group_messages gm " +
                     "JOIN users u ON gm.sender_id = u.user_id " +
                     "WHERE gm.group_id = ? " +
                     "ORDER BY gm.sent_at ASC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, groupId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User sender;
                    String role = rs.getString("role");
                    if ("Student".equalsIgnoreCase(role)) {
                        sender = new Student(rs.getInt("sender_id"), rs.getString("full_name"), rs.getString("email"), rs.getString("department"), rs.getString("roll_no_emp_id"));
                    } else {
                        sender = new Staff(rs.getInt("sender_id"), rs.getString("full_name"), rs.getString("email"), rs.getString("department"), rs.getString("roll_no_emp_id"));
                    }
                    
                    GroupMessage msg = new GroupMessage(
                        rs.getInt("message_id"),
                        rs.getInt("group_id"),
                        sender,
                        rs.getString("message_content"),
                        rs.getTimestamp("sent_at")
                    );
                    messages.add(msg);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    // 6. Search for users to add to a group (SELECT operation)
    public List<User> searchUsers(String query) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE full_name LIKE ? OR roll_no_emp_id LIKE ? OR email LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchTerm = "%" + query + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            pstmt.setString(3, searchTerm);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user;
                    String role = rs.getString("role");
                    if ("Student".equalsIgnoreCase(role)) {
                        user = new Student(rs.getInt("user_id"), rs.getString("full_name"), rs.getString("email"), rs.getString("department"), rs.getString("roll_no_emp_id"));
                    } else {
                        user = new Staff(rs.getInt("user_id"), rs.getString("full_name"), rs.getString("email"), rs.getString("department"), rs.getString("roll_no_emp_id"));
                    }
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // 7. Remove member from group (DELETE operation)
    public boolean removeMember(int groupId, int userId) {
        String sql = "DELETE FROM group_members WHERE group_id = ? AND user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, groupId);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
