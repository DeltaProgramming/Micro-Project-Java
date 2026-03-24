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

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WA_TEAL);
        headerPanel.setPreferredSize(new Dimension(400, 60));

        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        leftHeader.setOpaque(false);
        addBackButton(leftHeader);
        
        JLabel headerTitle = new JLabel("Reset Your Password");
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        leftHeader.add(headerTitle);
        headerPanel.add(leftHeader, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addLabelField(contentPanel, gbc, "College Email:", emailField = createTextField(20), row++);
        addLabelField(contentPanel, gbc, "New Password:", newPassField = createPasswordField(20), row++);
        addLabelField(contentPanel, gbc, "Confirm Password:", confirmPassField = createPasswordField(20), row++);

        JButton resetBtn = createWAButton("Reset Password", true);
        JButton cancelBtn = createWAButton("Cancel", false);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(resetBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        contentPanel.add(buttonPanel, gbc);

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
