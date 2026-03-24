package com.gect.connect.settings;

import com.gect.connect.db.SettingsManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;

/**
 * Settings screen for GECT Connect.
 * Part of Team 7: Notifications, Settings and Campus Feed Module.
 */
public class SettingsScreen extends BaseScreen {
    private User currentUser;
    private JCheckBox notificationCheck;
    private JComboBox<String> themeCombo;

    public SettingsScreen(User user) {
        super("Settings");
        this.currentUser = user;
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
        
        JLabel headerTitle = new JLabel("Settings");
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        leftHeader.add(headerTitle);
        headerPanel.add(leftHeader, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Profile Shortcut
        JPanel profileRow = new JPanel(new BorderLayout(15, 0));
        profileRow.setBackground(Color.WHITE);
        profileRow.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        profileRow.setMaximumSize(new Dimension(400, 80));
        
        JLabel picLabel = new JLabel("<html><div style='background-color:#128C7E; width:50px; height:50px; border-radius:25px; color:white; text-align:center;'><br>" + currentUser.getFullName().charAt(0) + "</div></html>");
        profileRow.add(picLabel, BorderLayout.WEST);
        
        JPanel profileText = new JPanel(new GridLayout(2, 1));
        profileText.setOpaque(false);
        JLabel nameLbl = new JLabel(currentUser.getFullName());
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel bioLbl = new JLabel(currentUser.getBio() != null ? currentUser.getBio() : "Available");
        bioLbl.setForeground(WA_GRAY);
        profileText.add(nameLbl);
        profileText.add(bioLbl);
        profileRow.add(profileText, BorderLayout.CENTER);
        
        contentPanel.add(profileRow);
        contentPanel.add(new JSeparator());

        // Settings Options
        contentPanel.add(createSettingsRow("Account", "Privacy, security, change number", "🔑"));
        contentPanel.add(createSettingsRow("Chats", "Theme, wallpapers, chat history", "💬"));
        contentPanel.add(createSettingsRow("Notifications", "Message, group & call tones", "🔔"));
        contentPanel.add(createSettingsRow("Storage and Data", "Network usage, auto-download", "💾"));
        contentPanel.add(createSettingsRow("Help", "Help center, contact us, privacy policy", "❓"));

        add(new JScrollPane(contentPanel), BorderLayout.CENTER);

        JButton saveBtn = createWAButton("SAVE CHANGES", true);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(saveBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // Listeners
        saveBtn.addActionListener(e -> {
            showInfo("Settings saved!");
            dispose();
        });
    }

    private JPanel createSettingsRow(String title, String desc, String icon) {
        JPanel row = new JPanel(new BorderLayout(20, 0));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(400, 70));
        row.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        row.add(iconLbl, BorderLayout.WEST);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        JLabel descLbl = new JLabel(desc);
        descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLbl.setForeground(WA_GRAY);
        textPanel.add(titleLbl);
        textPanel.add(descLbl);
        row.add(textPanel, BorderLayout.CENTER);

        return row;
    }
}
