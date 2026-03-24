package com.gect.connect.notifications;

import com.gect.connect.db.NotificationManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Notifications feed screen for GECT Connect.
 * Part of Team 6: Academic Notifications and Media Sharing Module.
 */
public class NotificationsFeedScreen extends BaseScreen {
    private User currentUser;
    private JPanel feedPanel;

    public NotificationsFeedScreen(User user) {
        super("Academic Notifications");
        this.currentUser = user;
        initUI();
        loadNotifications();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WA_TEAL);
        headerPanel.setPreferredSize(new Dimension(400, 60));

        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        leftHeader.setOpaque(false);
        addBackButton(leftHeader);
        
        JLabel headerTitle = new JLabel("Notifications");
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        leftHeader.add(headerTitle);
        headerPanel.add(leftHeader, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        feedPanel = new JPanel();
        feedPanel.setLayout(new BoxLayout(feedPanel, BoxLayout.Y_AXIS));
        feedPanel.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(feedPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton mediaBtn = createWAButton("GALLERY", false);
        actionPanel.add(mediaBtn);

        if ("Staff".equals(currentUser.getRole())) {
            JButton postBtn = createWAButton("POST NEW", true);
            postBtn.addActionListener(e -> new CreateAnnouncementScreen(currentUser, this).setVisible(true));
            actionPanel.add(postBtn);
        }
        
        add(actionPanel, BorderLayout.SOUTH);

        // Listeners
        mediaBtn.addActionListener(e -> new MediaGalleryScreen(currentUser).setVisible(true));
    }

    public void loadNotifications() {
        feedPanel.removeAll();
        List<NotificationManager.Notification> notifications = NotificationManager.getNotifications(currentUser.getDepartment());
        
        if (notifications.isEmpty()) {
            JLabel emptyLabel = new JLabel("No new notifications.");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            feedPanel.add(Box.createVerticalStrut(20));
            feedPanel.add(emptyLabel);
        } else {
            for (NotificationManager.Notification n : notifications) {
                feedPanel.add(createNotificationCard(n));
            }
        }
        feedPanel.revalidate();
        feedPanel.repaint();
    }

    private JPanel createNotificationCard(NotificationManager.Notification n) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, WA_LIGHT_GRAY),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(400, 200));

        JLabel titleLabel = new JLabel(n.title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(WA_TEAL);
        card.add(titleLabel);

        JLabel infoLabel = new JLabel("Posted by: " + n.senderName + " | " + n.sentAt.toString().substring(0, 16));
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        infoLabel.setForeground(WA_GRAY);
        card.add(infoLabel);

        card.add(Box.createVerticalStrut(10));

        JLabel contentLabel = new JLabel("<html><body style='width: 300px'>" + n.content + "</body></html>");
        contentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(contentLabel);

        if (n.filePath != null && !n.filePath.isEmpty()) {
            card.add(Box.createVerticalStrut(10));
            JButton downloadBtn = new JButton("📎 Download Attachment");
            downloadBtn.addActionListener(e -> showInfo("Downloading: " + n.filePath));
            card.add(downloadBtn);
        }

        return card;
    }
}
