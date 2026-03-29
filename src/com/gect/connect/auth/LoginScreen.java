package com.gect.connect.auth;

import com.gect.connect.db.AuthManager;
import com.gect.connect.gui.BaseScreen;
import com.gect.connect.model.User;
import java.awt.*;
import javax.swing.*;

/**
 * Login screen for GECT Connect.
 * Part of Team 1: User Registration and Authentication Module.
 */
public class LoginScreen extends BaseScreen {
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginScreen() {
        super("Login");
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        JLabel headerTitle = new JLabel("GECT Connect");
        styleHeader(headerPanel, headerTitle);
        // Login screen doesn't need back button to previous screen, 
        // but let's make it look consistent
        add(headerPanel, BorderLayout.NORTH);

        // Center Content
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_LIGHT);

        // Logo Section
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(BG_LIGHT);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        JLabel logoLabel = new JLabel("<html><div style='background-color:#3b82f6; width:80px; height:80px; border-radius:40px; color:white; text-align:center; font-family:Segoe UI;'><br><br><span style='font-size:24px; font-weight:bold;'>GECT</span></div></html>");
        logoPanel.add(logoLabel);
        mainPanel.add(logoPanel, BorderLayout.NORTH);

        // Form Section
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(BG_LIGHT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 30, 10, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row++;
        JLabel emailLabel = new JLabel("College Email");
        emailLabel.setForeground(TEXT_SECONDARY);
        formPanel.add(emailLabel, gbc);
        gbc.gridy = row++;
        emailField = createTextField(20);
        emailField.setToolTipText("Enter your @gectcr.ac.in email");
        formPanel.add(emailField, gbc);

        gbc.gridy = row++;
        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(TEXT_SECONDARY);
        formPanel.add(passLabel, gbc);
        gbc.gridy = row++;
        passwordField = createPasswordField(20);
        formPanel.add(passwordField, gbc);

        // Buttons
        gbc.gridy = row++;
        gbc.insets = new Insets(20, 30, 10, 30);
        JButton loginBtn = createWAButton("LOGIN", true);
        formPanel.add(loginBtn, gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(5, 30, 5, 30);
        JButton registerBtn = createWAButton("CREATE NEW ACCOUNT", false);
        formPanel.add(registerBtn, gbc);

        gbc.gridy = row++;
        JButton forgotBtn = new JButton("Forgot Password?");
        forgotBtn.setBorderPainted(false);
        forgotBtn.setContentAreaFilled(false);
        forgotBtn.setForeground(ACCENT_BLUE);
        forgotBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(forgotBtn, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Footer
        JLabel footerLabel = new JLabel("© 2026 GECT Connect - Campus Messenger", JLabel.CENTER);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        footerLabel.setForeground(TEXT_SECONDARY);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(footerLabel, BorderLayout.SOUTH);

        // Action Listeners
        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim().toLowerCase(); // Added trim() and toLowerCase()
            String password = new String(passwordField.getPassword());
            User user = AuthManager.login(email, password);
            if (user != null) {
                showInfo("Welcome, " + user.getFullName());
                // Navigate to Dashboard
                dispose();
                new Dashboard(user).setVisible(true);
            } else {
                showError("Invalid email or password!");
            }
        });

        registerBtn.addActionListener(e -> {
            dispose();
            new RegistrationForm().setVisible(true);
        });

        forgotBtn.addActionListener(e -> {
            new ForgotPasswordScreen().setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
    }
}
