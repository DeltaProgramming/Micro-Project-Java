package com.gect.connect.chat;

import com.gect.connect.db.ChatManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Individual chat window for GECT Connect.
 * Part of Team 4: Individual Chat Module.
 */
public class ChatWindow extends BaseScreen {
    private User currentUser;
    private User targetUser;
    private JPanel chatPanel;
    private JTextField messageField;
    private JScrollPane scrollPane;

    public ChatWindow(User currentUser, User targetUser) {
        super("Chat with " + targetUser.getFullName());
        this.currentUser = currentUser;
        this.targetUser = targetUser;
        initUI();
        startAutoRefresh();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Header (WhatsApp Style)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WA_TEAL);
        headerPanel.setPreferredSize(new Dimension(400, 60));

        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftHeader.setOpaque(false);
        
        addBackButton(leftHeader);

        // Circular Profile Icon
        JLabel profileIcon = new JLabel("<html><div style='background-color:#128C7E; width:40px; height:40px; border-radius:20px; color:white; text-align:center;'><br>" + targetUser.getFullName().charAt(0) + "</div></html>");
        leftHeader.add(profileIcon);

        JPanel namePanel = new JPanel(new GridLayout(2, 1));
        namePanel.setOpaque(false);
        JLabel titleLabel = new JLabel(targetUser.getFullName());
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel subtitleLabel = new JLabel("online");
        subtitleLabel.setForeground(new Color(255, 255, 255, 180));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        namePanel.add(titleLabel);
        namePanel.add(subtitleLabel);
        leftHeader.add(namePanel);
        
        headerPanel.add(leftHeader, BorderLayout.WEST);

        // Header Actions
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 18));
        rightHeader.setOpaque(false);
        JLabel callIcon = new JLabel("📞");
        callIcon.setForeground(Color.WHITE);
        JLabel videoIcon = new JLabel("📹");
        videoIcon.setForeground(Color.WHITE);
        JLabel moreIcon = new JLabel("⋮");
        moreIcon.setForeground(Color.WHITE);
        rightHeader.add(videoIcon);
        rightHeader.add(callIcon);
        rightHeader.add(moreIcon);
        headerPanel.add(rightHeader, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Chat Area
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(WA_CHAT_BG);
        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Input Area (WhatsApp Style)
        JPanel footerPanel = new JPanel(new BorderLayout(5, 5));
        footerPanel.setBackground(WA_CHAT_BG);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputArea = new JPanel(new BorderLayout(5, 5));
        inputArea.setBackground(Color.WHITE);
        inputArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 10), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel emojiBtn = new JLabel("😊");
        emojiBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        messageField = new JTextField();
        messageField.setBorder(null);
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel attachBtn = new JLabel("📎");
        attachBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        inputArea.add(emojiBtn, BorderLayout.WEST);
        inputArea.add(messageField, BorderLayout.CENTER);
        inputArea.add(attachBtn, BorderLayout.EAST);

        // Send Button (Circular)
        JButton sendBtn = new JButton("➤");
        sendBtn.setPreferredSize(new Dimension(45, 45));
        sendBtn.setBackground(WA_GREEN);
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setFocusPainted(false);
        sendBtn.setBorder(null);
        sendBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        footerPanel.add(inputArea, BorderLayout.CENTER);
        footerPanel.add(sendBtn, BorderLayout.EAST);
        add(footerPanel, BorderLayout.SOUTH);

        // Listeners
        sendBtn.addActionListener(e -> handleSendMessage());
        messageField.addActionListener(e -> handleSendMessage());

        loadMessages();
    }

    private void handleSendMessage() {
        String content = messageField.getText().trim();
        if (content.isEmpty()) return;

        if (ChatManager.sendMessage(currentUser.getUserId(), targetUser.getUserId(), content, "text")) {
            messageField.setText("");
            loadMessages();
        } else {
            showError("Failed to send message.");
        }
    }

    private void loadMessages() {
        List<ChatManager.Message> history = ChatManager.getChatHistory(currentUser.getUserId(), targetUser.getUserId());
        chatPanel.removeAll();
        for (ChatManager.Message msg : history) {
            chatPanel.add(createMessageBubble(msg));
        }
        chatPanel.revalidate();
        chatPanel.repaint();
        scrollToBottom();
    }

    private JPanel createMessageBubble(ChatManager.Message msg) {
        boolean isOutgoing = msg.senderId == currentUser.getUserId();
        JPanel row = new JPanel(new FlowLayout(isOutgoing ? FlowLayout.RIGHT : FlowLayout.LEFT));
        row.setBackground(WA_CHAT_BG);
        row.setMaximumSize(new Dimension(400, 100));

        JPanel bubble = new JPanel();
        bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
        bubble.setBackground(isOutgoing ? WA_OUTGOING : WA_INCOMING);
        bubble.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel contentLabel = new JLabel("<html><body style='width: 200px'>" + msg.content + "</body></html>");
        contentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bubble.add(contentLabel);

        JLabel timeLabel = new JLabel(msg.sentAt.toString().substring(11, 16));
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        timeLabel.setForeground(WA_GRAY);
        timeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        bubble.add(timeLabel);

        row.add(bubble);
        return row;
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    private void startAutoRefresh() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                loadMessages();
            }
        }, 5000, 5000); // Refresh every 5 seconds
    }
}
