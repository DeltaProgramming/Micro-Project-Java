package com.gect.connect.profile;

import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;

/**
 * Profile view screen for GECT Connect.
 * Part of Team 2: User Profile Management Module.
 */
public class ProfileViewScreen extends BaseScreen {
    private User user;

    public ProfileViewScreen(User user) {
        super("Profile");
        this.user = user;
        initUI();
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
        
        JLabel headerTitle = new JLabel("Profile");
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        leftHeader.add(headerTitle);
        headerPanel.add(leftHeader, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        // Profile Picture (Circle)
        JPanel picWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        picWrapper.setOpaque(false);
        JLabel picLabel = new JLabel("<html><div style='background-color:#128C7E; width:120px; height:120px; border-radius:60px; color:white; text-align:center; font-family:Segoe UI;'><br><br><span style='font-size:40px;'>" + user.getFullName().charAt(0) + "</span></div></html>");
        picWrapper.add(picLabel);
        contentPanel.add(picWrapper);
        contentPanel.add(Box.createVerticalStrut(30));

        // Info Rows
        contentPanel.add(createProfileRow("Name", user.getFullName(), "✎"));
        contentPanel.add(createProfileRow("About", user.getBio() != null ? user.getBio() : "Hey there! I am using GECT Connect.", "✎"));
        contentPanel.add(createProfileRow("Phone", user.getMobile() != null ? user.getMobile() : "Not set", "✎"));
        contentPanel.add(createProfileRow("Department", user.getDepartment(), ""));
        contentPanel.add(createProfileRow("Role", user.getRole(), ""));
        contentPanel.add(createProfileRow("Joined GECT Connect", user.getJoinDate().toString().substring(0, 10), ""));

        add(new JScrollPane(contentPanel), BorderLayout.CENTER);

        // Action Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JButton editBtn = createWAButton("EDIT PROFILE", true);
        JButton statsBtn = createWAButton("STATISTICS", false);
        
        buttonPanel.add(editBtn);
        buttonPanel.add(statsBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Listeners
        editBtn.addActionListener(e -> {
            dispose();
            new EditProfileScreen(user).setVisible(true);
        });
        statsBtn.addActionListener(e -> new AccountStatisticsScreen(user).setVisible(true));
    }

    private JPanel createProfileRow(String label, String value, String icon) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(400, 70));
        row.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(WA_GRAY);
        textPanel.add(lbl);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 15));
        textPanel.add(val);

        row.add(textPanel, BorderLayout.CENTER);

        if (!icon.isEmpty()) {
            JLabel iconLbl = new JLabel(icon);
            iconLbl.setForeground(WA_GREEN);
            iconLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
            row.add(iconLbl, BorderLayout.EAST);
        }

        return row;
    }
}
