package com.gect.connect.settings;

import com.gect.connect.db.SettingsManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import java.awt.*;
import javax.swing.*;

/**
 * Privacy settings screen for GECT Connect.
 * Part of Team 7: Notifications, Settings and Campus Feed Module.
 */
public class PrivacySettingsScreen extends BaseScreen {
    private User currentUser;
    private SettingsManager.UserSettings settings;
    private JComboBox<String> lastSeenCombo, profilePhotoCombo;

    public PrivacySettingsScreen(User user) {
        super("Privacy Settings");
        this.currentUser = user;
        this.settings = SettingsManager.getSettings(user.getUserId());
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("Privacy Controls");
        styleHeader(headerPanel, headerTitle);
        addBackButton(headerPanel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BG_LIGHT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lastSeenLabel = new JLabel("Who can see my last seen:");
        lastSeenLabel.setForeground(TEXT_MAIN);
        contentPanel.add(lastSeenLabel, gbc);
        gbc.gridx = 1;
        String[] options = {"Everyone", "Contacts", "Nobody"};
        lastSeenCombo = new JComboBox<>(options);
        lastSeenCombo.setSelectedItem(settings.privacyLastSeen);
        contentPanel.add(lastSeenCombo, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        JLabel photoLabel = new JLabel("Who can see my profile photo:");
        photoLabel.setForeground(TEXT_MAIN);
        contentPanel.add(photoLabel, gbc);
        gbc.gridx = 1;
        profilePhotoCombo = new JComboBox<>(options);
        profilePhotoCombo.setSelectedItem(settings.privacyProfilePhoto);
        contentPanel.add(profilePhotoCombo, gbc);
        row++;

        JButton saveBtn = createWAButton("Save Privacy Settings", true);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        contentPanel.add(saveBtn, gbc);
        row++;

        JButton cancelBtn = createWAButton("Cancel", false);
        contentPanel.add(cancelBtn, gbc);

        add(contentPanel, BorderLayout.CENTER);

        // Listeners
        saveBtn.addActionListener(e -> {
            settings.privacyLastSeen = (String) lastSeenCombo.getSelectedItem();
            settings.privacyProfilePhoto = (String) profilePhotoCombo.getSelectedItem();
            if (SettingsManager.updateSettings(settings)) {
                showInfo("Privacy settings updated!");
                dispose();
            } else {
                showError("Failed to update privacy settings.");
            }
        });
        cancelBtn.addActionListener(e -> dispose());
    }
}
