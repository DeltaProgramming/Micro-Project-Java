package com.gect.connect.groups;

import com.gect.connect.db.GroupManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.ChatGroup;
import com.gect.connect.model.GroupMessage;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Group chat window for GECT Connect.
 * Part of Team 5: Group Chat Module.
 */
public class GroupChatWindow extends BaseScreen {
    private User currentUser;
    private ChatGroup group;
    private GroupManager groupManager;
    private JPanel chatPanel;
    private JTextField messageField;
    private JScrollPane scrollPane;

    public GroupChatWindow(User currentUser, ChatGroup group) {
        super("Group: " + group.getGroupName());
        this.currentUser = currentUser;
        this.group = group;
        this.groupManager = new GroupManager();
        initUI();
        startAutoRefresh();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel(group.getGroupName());
        styleHeader(headerPanel, headerTitle);
        addBackButton(headerPanel);
        
        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftHeader.setOpaque(false);
        
        // Circular Profile Icon
        JLabel profileIcon = new JLabel("<html><div style='background-color:#3b82f6; width:40px; height:40px; border-radius:20px; color:white; text-align:center;'><br>" + group.getGroupName().charAt(0) + "</div></html>");
        leftHeader.add(profileIcon);

        JPanel namePanel = new JPanel(new GridLayout(2, 1));
        namePanel.setOpaque(false);
        JLabel titleLabel = new JLabel(group.getGroupName());
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel subtitleLabel = new JLabel("tap for group info");
        subtitleLabel.setForeground(new Color(255, 255, 255, 180));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        namePanel.add(titleLabel);
        namePanel.add(subtitleLabel);
        leftHeader.add(namePanel);
        
        headerPanel.add(leftHeader, BorderLayout.CENTER);
        
        // Open Group Info on header click
        headerPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        headerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new GroupInfoScreen(currentUser, group, null).setVisible(true);
            }
        });
        
        add(headerPanel, BorderLayout.NORTH);

        // Chat Area
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(BG_LIGHT);
        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Input Area (Modern Style)
        JPanel footerPanel = new JPanel(new BorderLayout(5, 5));
        footerPanel.setBackground(CARD_WHITE);
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, DIVIDER),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

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

        if (groupManager.sendGroupMessage(group.getGroupId(), currentUser.getUserId(), content)) {
            messageField.setText("");
            loadMessages();
        } else {
            showError("Failed to send group message.");
        }
    }

    private void loadMessages() {
        List<GroupMessage> history = groupManager.getGroupMessages(group.getGroupId());
        chatPanel.removeAll();
        for (GroupMessage msg : history) {
            chatPanel.add(createMessageBubble(msg));
        }
        chatPanel.revalidate();
        chatPanel.repaint();
        scrollToBottom();
    }

    private JPanel createMessageBubble(GroupMessage msg) {
        boolean isOutgoing = msg.getSender().getUserId() == currentUser.getUserId();
        JPanel row = new JPanel(new FlowLayout(isOutgoing ? FlowLayout.RIGHT : FlowLayout.LEFT));
        row.setBackground(BG_LIGHT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JPanel bubble = new JPanel();
        bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
        bubble.setBackground(isOutgoing ? WA_OUTGOING : CARD_WHITE);
        bubble.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DIVIDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        if (!isOutgoing) {
            JLabel senderLabel = new JLabel(msg.getSender().getFullName());
            senderLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            senderLabel.setForeground(ACCENT_BLUE);
            bubble.add(senderLabel);
        }

        JLabel contentLabel = new JLabel("<html><body style='width: 200px'>" + msg.getMessageContent() + "</body></html>");
        contentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentLabel.setForeground(TEXT_MAIN);
        bubble.add(contentLabel);

        String timeStr = "";
        if (msg.getSentAt() != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
            timeStr = sdf.format(msg.getSentAt());
        }
        JLabel timeLabel = new JLabel(timeStr);
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
        }, 5000, 5000);
    }
}
