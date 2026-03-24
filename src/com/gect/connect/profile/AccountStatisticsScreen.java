package com.gect.connect.profile;

import com.gect.connect.db.ProfileManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;

/**
 * Account statistics screen for GECT Connect.
 * Part of Team 2: User Profile Management Module.
 */
public class AccountStatisticsScreen extends BaseScreen {
    private User user;

    public AccountStatisticsScreen(User user) {
        super("Account Statistics");
        this.user = user;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("Account Stats");
        styleHeader(headerPanel, headerTitle);
        headerPanel.add(headerTitle);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Stats
        addStatRow(contentPanel, "Messages Sent:", String.valueOf(ProfileManager.getMessageCount(user.getUserId())));
        addStatRow(contentPanel, "Groups Joined:", String.valueOf(ProfileManager.getGroupCount(user.getUserId())));
        addStatRow(contentPanel, "Join Date:", user.getJoinDate().toString());
        addStatRow(contentPanel, "Last Active:", user.getLastSeen() != null ? user.getLastSeen().toString() : "Now");

        contentPanel.add(Box.createVerticalGlue());

        JButton closeBtn = createWAButton("Close", false);
        closeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(closeBtn);

        add(contentPanel, BorderLayout.CENTER);

        // Listeners
        closeBtn.addActionListener(e -> dispose());
    }

    private void addStatRow(JPanel panel, String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(400, 60));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(WA_GRAY);
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 18));
        val.setForeground(WA_GREEN);
        row.add(lbl, BorderLayout.NORTH);
        row.add(val, BorderLayout.CENTER);
        row.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(row);
    }
}
