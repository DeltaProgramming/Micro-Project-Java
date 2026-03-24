package com.gect.connect.profile;

import com.gect.connect.db.ProfileManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import javax.swing.*;
import java.awt.*;

/**
 * Status update screen for GECT Connect.
 * Part of Team 2: User Profile Management Module.
 */
public class StatusUpdateScreen extends BaseScreen {
    private User user;
    private JTextArea statusArea;

    public StatusUpdateScreen(User user) {
        super("Update Status");
        this.user = user;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("Status Update");
        styleHeader(headerPanel, headerTitle);
        headerPanel.add(headerTitle);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(new JLabel("Compose Status:"), gbc);
        gbc.gridy = 1;
        statusArea = new JTextArea(4, 20);
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);
        statusArea.setText(user.getBio());
        contentPanel.add(new JScrollPane(statusArea), gbc);

        // Emoji Picker (Simulated)
        JButton emojiBtn = new JButton("Add Emoji 😃");
        emojiBtn.setForeground(WA_GREEN); // Set text color to green
        emojiBtn.setBackground(Color.WHITE);
        emojiBtn.setFocusPainted(false);
        emojiBtn.addActionListener(e -> statusArea.append(" 😃"));
        gbc.gridy = 2;
        contentPanel.add(emojiBtn, gbc);

        JButton saveBtn = createWAButton("Update Status", true);
        JButton cancelBtn = createWAButton("Cancel", false);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridy = 3;
        contentPanel.add(buttonPanel, gbc);

        add(contentPanel, BorderLayout.CENTER);

        // Listeners
        saveBtn.addActionListener(e -> {
            String status = statusArea.getText();
            if (ProfileManager.updateStatus(user.getUserId(), status)) {
                user.setBio(status);
                showInfo("Status updated!");
                dispose();
            } else {
                showError("Failed to update status.");
            }
        });

        cancelBtn.addActionListener(e -> dispose());
    }
}
