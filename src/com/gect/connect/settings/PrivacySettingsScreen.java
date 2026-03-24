package com.gect.connect.settings;

import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;

/**
 * Privacy settings screen for GECT Connect.
 * Part of Team 7: Notifications, Settings and Campus Feed Module.
 */
public class PrivacySettingsScreen extends BaseScreen {
    private User currentUser;
    private JComboBox<String> lastSeenCombo, profilePhotoCombo;

    public PrivacySettingsScreen(User user) {
        super("Privacy Settings");
        this.currentUser = user;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("Privacy Controls");
        styleHeader(headerPanel, headerTitle);
        headerPanel.add(headerTitle);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(new JLabel("Who can see my last seen:"), gbc);
        gbc.gridx = 1;
        String[] options = {"Everyone", "Contacts", "Nobody"};
        lastSeenCombo = new JComboBox<>(options);
        contentPanel.add(lastSeenCombo, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(new JLabel("Who can see my profile photo:"), gbc);
        gbc.gridx = 1;
        profilePhotoCombo = new JComboBox<>(options);
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
            showInfo("Privacy settings updated!");
            dispose();
        });
        cancelBtn.addActionListener(e -> dispose());
    }
}
