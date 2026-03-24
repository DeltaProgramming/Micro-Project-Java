package com.gect.connect.settings;

import com.gect.connect.db.SettingsManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Campus events feed screen for GECT Connect.
 * Part of Team 7: Notifications, Settings and Campus Feed Module.
 */
public class CampusEventsFeedScreen extends BaseScreen {
    private User currentUser;
    private JPanel eventsPanel;

    public CampusEventsFeedScreen(User user) {
        super("Campus Events");
        this.currentUser = user;
        initUI();
        loadEvents();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("GECT Events Feed");
        styleHeader(headerPanel, headerTitle);
        headerPanel.add(headerTitle);
        add(headerPanel, BorderLayout.NORTH);

        eventsPanel = new JPanel();
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
        eventsPanel.setBackground(Color.WHITE);
        add(new JScrollPane(eventsPanel), BorderLayout.CENTER);

        JButton closeBtn = createWAButton("Back to Settings", false);
        closeBtn.addActionListener(e -> dispose());
        add(closeBtn, BorderLayout.SOUTH);
    }

    private void loadEvents() {
        eventsPanel.removeAll();
        List<SettingsManager.CampusEvent> events = SettingsManager.getCampusEvents();
        
        if (events.isEmpty()) {
            JLabel emptyLabel = new JLabel("No upcoming events.");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            eventsPanel.add(Box.createVerticalStrut(20));
            eventsPanel.add(emptyLabel);
        } else {
            for (SettingsManager.CampusEvent e : events) {
                eventsPanel.add(createEventCard(e));
            }
        }
        eventsPanel.revalidate();
        eventsPanel.repaint();
    }

    private JPanel createEventCard(SettingsManager.CampusEvent e) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, WA_LIGHT_GRAY),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(400, 200));

        JLabel titleLabel = new JLabel(e.title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(WA_GREEN);
        card.add(titleLabel);

        JLabel infoLabel = new JLabel("📅 " + e.eventDate.toString().substring(0, 16) + " | 📍 " + e.location);
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        infoLabel.setForeground(WA_GRAY);
        card.add(infoLabel);

        card.add(Box.createVerticalStrut(10));

        JLabel descLabel = new JLabel("<html><body style='width: 300px'>" + e.description + "</body></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(descLabel);

        return card;
    }
}
