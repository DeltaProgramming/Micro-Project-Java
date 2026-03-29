package com.gect.connect.auth;

import com.gect.connect.db.AuthManager;
import com.gect.connect.gui.BaseScreen;
import javax.swing.*;
import java.awt.*;

/**
 * Forgot password screen for GECT Connect.
 * Part of Team 1: User Registration and Authentication Module.
 */
public class ForgotPasswordScreen extends BaseScreen {
    private JTextField emailField;
    private JPasswordField newPassField, confirmPassField;

    public ForgotPasswordScreen() {
        super("Reset Password");
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("Reset Password");
        styleHeader(headerPanel, headerTitle);
        addBackButton(headerPanel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BG_LIGHT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 25, 10, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel emailLabel = new JLabel("College Email");
        emailLabel.setForeground(TEXT_SECONDARY);
        contentPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        emailField = createTextField(20);
        contentPanel.add(emailField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        JLabel passLabel = new JLabel("New Password");
        passLabel.setForeground(TEXT_SECONDARY);
        contentPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        newPassField = createPasswordField(20);
        contentPanel.add(newPassField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        JLabel confirmLabel = new JLabel("Confirm Password");
        confirmLabel.setForeground(TEXT_SECONDARY);
        contentPanel.add(confirmLabel, gbc);
        gbc.gridx = 1;
        confirmPassField = createPasswordField(20);
        contentPanel.add(confirmPassField, gbc);
        row++;

        JButton resetBtn = createWAButton("Reset Password", true);
        JButton cancelBtn = createWAButton("Cancel", false);
        
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 25, 10, 25);
        contentPanel.add(resetBtn, gbc);
        row++;
        
        gbc.insets = new Insets(5, 25, 25, 25);
        contentPanel.add(cancelBtn, gbc);

        add(contentPanel, BorderLayout.CENTER);

        // Listeners
        resetBtn.addActionListener(e -> handlePasswordReset());
        cancelBtn.addActionListener(e -> dispose());
    }

    private void addLabelField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent component, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private void handlePasswordReset() {
        String email = emailField.getText().trim().toLowerCase(); // Trim and lower case
        String newPass = new String(newPassField.getPassword());
        String confirmPass = new String(confirmPassField.getPassword());

        if (email.isEmpty() || newPass.isEmpty()) {
            showError("Please fill all fields.");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            showError("Passwords do not match!");
            return;
        }

        if (AuthManager.updatePassword(email, newPass)) {
            showInfo("Password reset successful! Please login.");
            dispose();
        } else {
            showError("Failed to reset password. Check your email.");
        }
    }
}
