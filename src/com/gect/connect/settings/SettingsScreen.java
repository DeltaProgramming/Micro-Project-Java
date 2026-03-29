package com.gect.connect.settings;

import com.gect.connect.db.SettingsManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.gui.UIUtils;
import com.gect.connect.model.User;
import java.awt.*;
import javax.swing.*;

/**
 * Settings screen for GECT Connect.
 * Part of Team 7: Notifications, Settings and Campus Feed Module.
 */
public class SettingsScreen extends BaseScreen {
    private User currentUser;
    private SettingsManager.UserSettings settings;

    public SettingsScreen(User user) {
        super("Settings");
        this.currentUser = user;
        this.settings = SettingsManager.getSettings(user.getUserId());
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("Settings");
        styleHeader(headerPanel, headerTitle);
        addBackButton(headerPanel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG_LIGHT);

        // Profile Shortcut
        JPanel profileRow = new JPanel(new BorderLayout(15, 0));
        profileRow.setBackground(CARD_WHITE);
        profileRow.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        profileRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JLabel picLabel = new JLabel("<html><div style='background-color:#3b82f6; width:50px; height:50px; border-radius:25px; color:white; text-align:center;'><br>" + currentUser.getFullName().charAt(0) + "</div></html>");
        profileRow.add(picLabel, BorderLayout.WEST);
        
        JPanel profileText = new JPanel(new GridLayout(2, 1));
        profileText.setOpaque(false);
        JLabel nameLbl = new JLabel(currentUser.getFullName());
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLbl.setForeground(TEXT_MAIN);
        JLabel bioLbl = new JLabel(currentUser.getBio() != null && !currentUser.getBio().isEmpty() ? currentUser.getBio() : "Available");
        bioLbl.setForeground(TEXT_SECONDARY);
        profileText.add(nameLbl);
        profileText.add(bioLbl);
        profileRow.add(profileText, BorderLayout.CENTER);
        
        contentPanel.add(profileRow);
        contentPanel.add(new JSeparator());

        // Settings Options (Modular)
        contentPanel.add(UIUtils.createSettingsRow("Account", "Privacy, security, change number", "🔑", () -> {
            new PrivacySettingsScreen(currentUser).setVisible(true);
        }));

        contentPanel.add(UIUtils.createSettingsRow("Chats", "Theme, wallpapers, chat history", "💬", () -> {
            showThemeSelector();
        }));

        contentPanel.add(UIUtils.createSettingsRow("Notifications", "Message, group & call tones", "🔔", () -> {
            showNotificationSettings();
        }));

        contentPanel.add(UIUtils.createSettingsRow("Help", "Help center, contact us, privacy policy", "❓", () -> {
            new HelpAboutScreen(currentUser).setVisible(true);
        }));

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        JButton saveBtn = createWAButton("SAVE CHANGES", true);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(CARD_WHITE);
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, DIVIDER));
        btnPanel.add(saveBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // Listeners
        saveBtn.addActionListener(e -> {
            if (SettingsManager.updateSettings(settings)) {
                showInfo("Settings saved successfully!");
                dispose();
            } else {
                showError("Failed to save settings.");
            }
        });
    }

    private void showThemeSelector() {
        String[] themes = {"Light", "Dark"};
        String selected = (String) JOptionPane.showInputDialog(this, 
            "Select Theme", "Theme Selector", JOptionPane.QUESTION_MESSAGE, 
            null, themes, settings.theme);
        
        if (selected != null) {
            settings.theme = selected;
            showInfo("Theme changed to " + selected + ". Restart app to apply.");
        }
    }

    private void showNotificationSettings() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        
        JCheckBox notifCheck = new JCheckBox("Enable Notifications", settings.notificationEnabled);
        
        JPanel soundPanel = new JPanel(new BorderLayout(5, 5));
        soundPanel.add(new JLabel("Notification Tone:"), BorderLayout.WEST);
        
        String[] sounds = {"Default", "Chime", "Alert", "Modern", "Classic", "Digital"};
        JComboBox<String> soundCombo = new JComboBox<>(sounds);
        soundCombo.setSelectedItem(settings.notificationSound);
        soundPanel.add(soundCombo, BorderLayout.CENTER);
        
        panel.add(notifCheck);
        panel.add(new JLabel(" ")); // Spacer
        panel.add(soundPanel);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Notification Preferences", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            settings.notificationEnabled = notifCheck.isSelected();
            settings.notificationSound = (String) soundCombo.getSelectedItem();
        }
    }
}
