package com.gect.connect.chat;

import com.gect.connect.db.ChatManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

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

        // Header
        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel(targetUser.getFullName());
        styleHeader(headerPanel, headerTitle);
        addBackButton(headerPanel);
        add(headerPanel, BorderLayout.NORTH);

        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(BG_LIGHT);
        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Input Area (Modern Style)
        JPanel footerPanel = new JPanel(new BorderLayout(5, 5));
        footerPanel.setBackground(CARD_WHITE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        messageField = createTextField(20);
        messageField.setToolTipText("Type a message...");
        
        JButton sendBtn = createWAButton("SEND", true);
        
        footerPanel.add(messageField, BorderLayout.CENTER);
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
        row.setBackground(BG_LIGHT);
        row.setMaximumSize(new Dimension(400, 100));

        JPanel bubble = new JPanel();
        bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
        bubble.setBackground(isOutgoing ? WA_OUTGOING : CARD_WHITE);
        bubble.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DIVIDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JLabel contentLabel = new JLabel("<html><body style='width: 200px'>" + msg.content + "</body></html>");
        contentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentLabel.setForeground(TEXT_MAIN);
        bubble.add(contentLabel);

        JLabel timeLabel = new JLabel(msg.sentAt.toString().substring(11, 16));
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        timeLabel.setForeground(TEXT_SECONDARY);
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
