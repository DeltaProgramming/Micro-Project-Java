package com.gect.connect.model;

import java.util.Date;

/**
 * GroupMessage class representing a message in a group.
 * Demonstrates: Composition (has a sender and a group)
 */
public class GroupMessage {
    private int messageId;
    private int groupId;
    private User sender;
    private String messageContent;
    private Date sentAt;

    public GroupMessage() {}

    public GroupMessage(int messageId, int groupId, User sender, String messageContent, Date sentAt) {
        this.messageId = messageId;
        this.groupId = groupId;
        this.sender = sender;
        this.messageContent = messageContent;
        this.sentAt = sentAt;
    }

    // Getters and Setters
    public int getMessageId() { return messageId; }
    public void setMessageId(int messageId) { this.messageId = messageId; }

    public int getGroupId() { return groupId; }
    public void setGroupId(int groupId) { this.groupId = groupId; }

    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }

    public String getMessageContent() { return messageContent; }
    public void setMessageContent(String messageContent) { this.messageContent = messageContent; }

    public Date getSentAt() { return sentAt; }
    public void setSentAt(Date sentAt) { this.sentAt = sentAt; }

    @Override
    public String toString() {
        return sender.getFullName() + ": " + messageContent;
    }
}
