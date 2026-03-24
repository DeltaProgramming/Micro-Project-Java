package com.gect.connect.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ChatGroup class for managing group messaging.
 * Demonstrates: Composition (list of users), Polymorphism (handling different user types)
 */
public class ChatGroup {
    private int groupId;
    private String groupName;
    private String description;
    private User createdBy;
    private Date createdAt;
    private String groupType;
    private List<User> members;

    public ChatGroup() {
        this.members = new ArrayList<>();
    }

    public ChatGroup(int groupId, String groupName, String description, String groupType) {
        this();
        this.groupId = groupId;
        this.groupName = groupName;
        this.description = description;
        this.groupType = groupType;
    }

    // Getters and Setters
    public int getGroupId() { return groupId; }
    public void setGroupId(int groupId) { this.groupId = groupId; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public String getGroupType() { return groupType; }
    public void setGroupType(String groupType) { this.groupType = groupType; }

    public List<User> getMembers() { return members; }
    public void setMembers(List<User> members) { this.members = members; }

    // Add a member to the group (Polymorphism - can be Student or Staff)
    public void addMember(User user) {
        this.members.add(user);
    }

    @Override
    public String toString() {
        return groupName + " (" + groupType + ")";
    }
}
