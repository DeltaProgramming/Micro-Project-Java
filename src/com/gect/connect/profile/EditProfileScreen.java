package com.gect.connect.profile;

import com.gect.connect.db.ProfileManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;

/**
 * Edit profile screen for GECT Connect.
 * Part of Team 2: User Profile Management Module.
 */
public class EditProfileScreen extends BaseScreen {
    private User user;
    private JTextField nameField, mobileField;
    private JTextArea bioArea;

    public EditProfileScreen(User user) {
        super("Edit Profile");
        this.user = user;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("Edit Profile");
        styleHeader(headerPanel, headerTitle);
        headerPanel.add(headerTitle);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addLabelField(contentPanel, gbc, "Full Name:", nameField = createTextField(20), row++);
        nameField.setText(user.getFullName());

        addLabelField(contentPanel, gbc, "Mobile:", mobileField = createTextField(20), row++);
        mobileField.setText(user.getMobile());

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        contentPanel.add(new JLabel("Bio / Status:"), gbc);
        gbc.gridx = 1;
        bioArea = new JTextArea(3, 20);
        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);
        bioArea.setText(user.getBio());
        contentPanel.add(new JScrollPane(bioArea), gbc);
        row++;

        JButton saveBtn = createWAButton("Save Changes", true);
        JButton changePicBtn = createWAButton("Change Profile Picture", false);
        JButton cancelBtn = createWAButton("Cancel", false);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveBtn);
        buttonPanel.add(changePicBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        contentPanel.add(buttonPanel, gbc);

        add(contentPanel, BorderLayout.CENTER);

        // Listeners
        saveBtn.addActionListener(e -> handleSave());
        changePicBtn.addActionListener(e -> new ChangeProfilePictureScreen(user).setVisible(true));
        cancelBtn.addActionListener(e -> {
            dispose();
            new ProfileViewScreen(user).setVisible(true);
        });
    }

    private void addLabelField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent component, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private void handleSave() {
        user.setFullName(nameField.getText());
        user.setMobile(mobileField.getText());
        user.setBio(bioArea.getText());

        if (ProfileManager.updateProfile(user)) {
            showInfo("Profile updated successfully!");
            dispose();
            new ProfileViewScreen(user).setVisible(true);
        } else {
            showError("Failed to update profile.");
        }
    }
}
