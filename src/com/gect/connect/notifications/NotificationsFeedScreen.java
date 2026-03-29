package com.gect.connect.notifications;

import com.gect.connect.db.NotificationManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.gui.UIUtils;
import com.gect.connect.model.User;
import java.awt.*;
import java.util.List;
import javax.swing.*;

/**
 * Notifications feed screen for GECT Connect.
 * Part of Team 7: Notifications, Settings and Campus Feed Module.
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
        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("Notification Center");
        styleHeader(headerPanel, headerTitle);
        addBackButton(headerPanel);
        add(headerPanel, BorderLayout.NORTH);

        feedPanel = new JPanel();
        feedPanel.setLayout(new BoxLayout(feedPanel, BoxLayout.Y_AXIS));
        feedPanel.setBackground(BG_LIGHT);
        JScrollPane scrollPane = new JScrollPane(feedPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        actionPanel.setBackground(CARD_WHITE);
        actionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, DIVIDER),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
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
        List<NotificationManager.Notification> notifications = NotificationManager.getNotificationsForUser(currentUser);
        
        if (notifications.isEmpty()) {
            JLabel emptyLabel = new JLabel("No new notifications.");
            emptyLabel.setHorizontalAlignment(JLabel.CENTER);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            feedPanel.add(Box.createVerticalStrut(50));
            feedPanel.add(emptyLabel);
        } else {
            for (NotificationManager.Notification n : notifications) {
                String time = n.sentAt.toString().substring(0, 16);
                String title = n.title + " (from " + n.senderName + ")";
                JPanel card = UIUtils.createNotificationCard(title, n.content, time, n.isRead, () -> {
                    NotificationManager.markAsRead(n.notificationId, currentUser.getUserId());
                });
                feedPanel.add(card);
            }
        }
        feedPanel.revalidate();
        feedPanel.repaint();
    }
}
